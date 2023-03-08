package net.kelsier.bookshelf.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.sslreload.SslReloadBundle;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import net.kelsier.bookshelf.api.resource.bookshelf.Authors;
import net.kelsier.bookshelf.api.resource.bookshelf.BookDetails;
import net.kelsier.bookshelf.api.resource.bookshelf.BookSeries;
import net.kelsier.bookshelf.api.resource.bookshelf.Books;
import net.kelsier.bookshelf.api.resource.bookshelf.Comments;
import net.kelsier.bookshelf.api.resource.bookshelf.Data;
import net.kelsier.bookshelf.api.resource.bookshelf.Languages;
import net.kelsier.bookshelf.api.resource.bookshelf.Publishers;
import net.kelsier.bookshelf.api.resource.bookshelf.Ratings;
import net.kelsier.bookshelf.api.resource.bookshelf.Tags;
import net.kelsier.bookshelf.framework.auth.BasicAuthenticator;
import net.kelsier.bookshelf.framework.auth.BasicAuthorizer;
import net.kelsier.bookshelf.framework.auth.UserAuth;
import net.kelsier.bookshelf.framework.config.AuthConfiguration;
import net.kelsier.bookshelf.framework.config.DatasourceConfiguration;
import net.kelsier.bookshelf.framework.config.EncryptionConfiguration;
import net.kelsier.bookshelf.framework.config.DenialOfServiceConfiguration;
import net.kelsier.bookshelf.framework.config.MetaBooksConfiguration;
import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;
import net.kelsier.bookshelf.framework.db.dao.users.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.users.UserDAO;
import net.kelsier.bookshelf.framework.encryption.JasyptCipher;
import net.kelsier.bookshelf.framework.environment.ResourceRegistrar;
import net.kelsier.bookshelf.framework.error.exception.DatabaseExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.JsonProcessingExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.ResponseErrorExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.RuntimeExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.StartupException;
import net.kelsier.bookshelf.framework.error.exception.TechnicalExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.ValidationExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.WebApplicationExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.WebApplicationSilentExceptionMapper;
import net.kelsier.bookshelf.framework.filter.CacheControlFilter;
import net.kelsier.bookshelf.framework.filter.CsrfFilter;
import net.kelsier.bookshelf.framework.health.DatabaseHealth;
import net.kelsier.bookshelf.framework.loaders.ConfigLoader;
import net.kelsier.bookshelf.framework.loaders.YamlConfigLoader;
import net.kelsier.bookshelf.framework.log.LogColour;
import net.kelsier.bookshelf.framework.openapi.OpenApi;
import net.kelsier.bookshelf.framework.resource.Login;
import net.kelsier.bookshelf.framework.resource.RoleAdministration;
import net.kelsier.bookshelf.framework.resource.UserAdministration;
import net.kelsier.bookshelf.framework.tasks.EncrypterTask;
import net.kelsier.bookshelf.migration.resource.BookshelfAdministration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.servlets.DoSFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import org.pac4j.dropwizard.Pac4jBundle;
import org.pac4j.dropwizard.Pac4jFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.exit;

public class MetaBooks extends Application<MetaBooksConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaBooks.class);

    private ResourceRegistrar resourceRegistrar;

    private final ClassLoader classLoader;

    private Jdbi databaseConnection;

    private JasyptCipher cipher;

    @Valid
    private final Pac4jBundle<MetaBooksConfiguration> pac4j = new Pac4jBundle<>() {
        @Override
        public Pac4jFactory getPac4jFactory(@Valid MetaBooksConfiguration configuration) {
            return configuration.getPac4jFactory();
        }
    };

    /**
     * @param classLoader used to retrieve version information from the manifest
     */
    public MetaBooks(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static void main(final String[] args) {
        try {
            new MetaBooks(Thread.currentThread().getContextClassLoader()).run(args);
        } catch (final Exception e) {
            LOGGER.error("Failed to initialize", e);
            exit(1);
        }
    }

    @Override
    public String getName() {
        return "MetaBooks";
    }

    @Override
    public void initialize(final @Valid Bootstrap<MetaBooksConfiguration> bootstrap) {
        addBundles(bootstrap);

        //Exclude null values from response JSON objects by default
        //this can be overridden using the annotation @JsonInclude(JsonInclude.Include.ALWAYS)
        final ObjectMapper objectMapper = bootstrap.getObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void run(
        @Valid final MetaBooksConfiguration configuration,
        final Environment environment) {
        resourceRegistrar = new ResourceRegistrar(environment);

        try {
            startServer(configuration, environment);
        } catch (final ConfigurationException e) {
            final String logMessage = MessageFormat.format(
                "{0}{1}{2}",
                LogColour.ANSI_RED,
                e.getMessage(),
                LogColour.ANSI_RESET);
            LOGGER.error("\n\n{}\n\n", logMessage);
            throw new StartupException(logMessage, e);
        }
    }

    private void addBundles(Bootstrap<MetaBooksConfiguration> bootstrap) {
        bootstrap.addBundle(pac4j);

        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(@Valid MetaBooksConfiguration configuration) {
                return new YamlConfigLoader(configuration.getConfigPath(),getJasyptCipher(configuration)).
                    loadConfiguration(DatasourceConfiguration.class);
            }
        });

        /* Bundle that gathers all the ssl connectors and registers an admin task that will
        refresh ssl configuration on request */
        bootstrap.addBundle(new SslReloadBundle());

        // Allow variable substitution in config
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );

        bootstrap.addBundle(new JdbiExceptionsBundle());

        // Add Swagger as a resource
        bootstrap.addBundle(new AssetsBundle("/swagger3/", "/swagger", "index.html", "swagger"));
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    /**
     * Start Server
     *
     * @param configuration yaml configuration
     * @param environment Dropwizard Environment
     * @throws ConfigurationException Thrown when there was a configuration error
     */
    private void startServer(final MetaBooksConfiguration configuration, final Environment environment) throws ConfigurationException {
        //Create a cipher to handle encrypted configuration
        cipher = getJasyptCipher(configuration);

        // Configuration loader for yaml -> pojo
        final ConfigLoader configLoader = new YamlConfigLoader(
            configuration.getConfigPath(),
            cipher
        );

        // Register custom Exception Mappers
        registerExceptionMappers();

        // Disable caching
        setCacheHeaders(environment);

        // Anti CSRF Filtering
        setAntiCSRFFilter(environment);

        // Create CORS and DOS Filter
        setupFilters(configuration, environment, configLoader);

        // Add tasks to the environment
        addToEnvironment(environment);

        registerOpenAPI(environment);

        if (configuration.isDatabaseEnabled()) {
            databaseConnection = getJdbiFactory(configLoader.loadConfiguration(DatasourceConfiguration.class),environment);
        } else {
            LOGGER.warn("{}Unable to connect to database, functionality will be limited{}",LogColour.ANSI_YELLOW, LogColour.ANSI_RESET);
        }

        // Configure basic authentication
        configureBasicAuth(
            configLoader.loadConfiguration(AuthConfiguration.class),
            configLoader.loadConfiguration(EncryptionConfiguration.class),
            environment);

        // Register resources

        registerRestResources(configLoader.loadConfiguration(EncryptionConfiguration.class));

        // Register health checks
        if (null != databaseConnection) {
            registerHealthChecks(environment, getRoleDao());
        }

        MetaBooksInfo info = new MetaBooksInfo(classLoader);

        LOGGER.info(
            "{}{} version {} has started{}",
            LogColour.ANSI_PURPLE,
            info.getTitle(),
            info.getVersion(),
            LogColour.ANSI_RESET);
        LOGGER.debug(
            "{}Java version: {} \t Build time: {}{}",
            LogColour.ANSI_RED,
            info.getJavaVersion(),
            info.getBuildTime(),
            LogColour.ANSI_RESET);
    }

    private static JasyptCipher getJasyptCipher(final MetaBooksConfiguration configuration) {
        if (null != System.getProperty("CIPHER")) {
            return new JasyptCipher(System.getProperty("CIPHER"));
        } else if (null != configuration.getCipherPass()) {
            LOGGER.warn("{}Setting cipher password from configuration is insecure. " +
                "It is recommended to pass in the cipher through system properties{}",
                LogColour.ANSI_RED, LogColour.ANSI_RESET);
            return new JasyptCipher(configuration.getCipherPass());
        } else {
            LOGGER.info("Not using encrypted configuration.");
            return null;
        }
    }

    private static Jdbi getJdbiFactory(DataSourceFactory datasourceConfiguration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        return factory.build(
            environment,
            datasourceConfiguration,
            "postgresql");
    }

    private RoleDAO getRoleDao() {
        return databaseConnection.onDemand(RoleDAO.class);
    }

    private UserDAO getUserDao() {
        return databaseConnection.onDemand(UserDAO.class);
    }

    private void registerExceptionMappers() {
        // Register external exception mappers, these map exceptions to JSON responses
        resourceRegistrar.registerResource(new WebApplicationExceptionMapper());
        resourceRegistrar.registerResource(new WebApplicationSilentExceptionMapper());
        resourceRegistrar.registerResource(new ValidationExceptionMapper());
        resourceRegistrar.registerResource(new TechnicalExceptionMapper());
        resourceRegistrar.registerResource(new RuntimeExceptionMapper());
        resourceRegistrar.registerResource(new ResponseErrorExceptionMapper());
        resourceRegistrar.registerResource(new DatabaseExceptionMapper());
        resourceRegistrar.registerResource(new JsonProcessingExceptionMapper());
    }

    private void addToEnvironment(Environment environment) {
        //Enable multipart form data
        resourceRegistrar.registerResource(MultiPartFeature.class);

        environment.admin().addTask(new EncrypterTask(cipher));
    }

    private static void setupFilters(MetaBooksConfiguration configuration, Environment environment, ConfigLoader configLoader) throws ConfigurationException {
        // Setup CORS filter
        setUpCorsFilter(configuration, environment);

        // Setup DoS filter
        setUpDosFilter(environment, configLoader);
    }

    private void registerOpenAPI(final Environment environment) {
        //Stream.of("net.kelsier.bookshelf.migration.resource")

        OpenApi.configure(environment, "/api", Stream.of("net.kelsier.bookshelf").collect(
                Collectors.toSet()));

//        OpenApi.configure(environment, "/api", Stream.of("net.kelsier.bookshelf.api.resource").collect(
//            Collectors.toSet()));

        final OpenAPI oas = new OpenAPI();
        final Info info = new Info()
            .title("Swagger Sample App")
            .description(
                "Gather information about a domain, including optional DNS, IP and whois information." +
                    "The information gathered is publicly available, and should be used for education purposed only.")
            .contact(new Contact()
                .email("kelsier.luthadel@protonmail.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html"));


        oas.info(info);

        final List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("/api"));
        oas.servers(servers);
        final SwaggerConfiguration oasConfig = new SwaggerConfiguration()
            .openAPI(oas)
            .prettyPrint(true)
            .resourcePackages(Stream.of("net.kelsier.bookserver.api.rest")
                .collect(Collectors.toSet()));

        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);


        resourceRegistrar.registerResource(new OpenApiResource().openApiConfiguration(oasConfig));
        environment.jersey().register(new OpenApiResource().openApiConfiguration(oasConfig));
    }

    private void registerRestResources(final EncryptionConfiguration encryptionConfiguration) {
        if (null == databaseConnection) {
            LOGGER.warn("{}Unable to connect to database, some REST resources will not be loaded{}",LogColour.ANSI_YELLOW, LogColour.ANSI_RESET);
        } else {
            resourceRegistrar.registerResource(new Login(getUserDao(), encryptionConfiguration));
            resourceRegistrar.registerResource(new UserAdministration(
                getUserDao(),
                getRoleDao(),
                encryptionConfiguration));
            resourceRegistrar.registerResource(new RoleAdministration(getRoleDao()));
            resourceRegistrar.registerResource(new Books(databaseConnection));
            resourceRegistrar.registerResource(new Authors(databaseConnection));
            resourceRegistrar.registerResource(new Comments(databaseConnection));
            resourceRegistrar.registerResource(new Data(databaseConnection));
            resourceRegistrar.registerResource(new Languages(databaseConnection));
            resourceRegistrar.registerResource(new Publishers(databaseConnection));
            resourceRegistrar.registerResource(new BookSeries(databaseConnection));
            resourceRegistrar.registerResource(new Ratings(databaseConnection));
            resourceRegistrar.registerResource(new Tags(databaseConnection));
            resourceRegistrar.registerResource(new BookDetails(databaseConnection));

            resourceRegistrar.registerResource(new BookshelfAdministration(databaseConnection));
        }
    }

    private static void registerHealthChecks(final Environment environment, final RoleDAO roleDAO) {
        environment.healthChecks().register(
            MetaBooks.class.getCanonicalName() + "database.connection",
            new DatabaseHealth(roleDAO));
    }

    /**
     * Add headers to REST responses to force cache to invalidate
     *
     * @param environment Dropwizard environment
     */
    private static void setCacheHeaders(final Environment environment) {
        final FilterRegistration.Dynamic filter = environment.servlets()
            .addFilter("cacheControlFilter", new CacheControlFilter());
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private static void setAntiCSRFFilter(final Environment environment) {
        environment.servlets().addFilter("csrfFilter", new CsrfFilter())
            .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }

    /**
     * Set up DoS filter.
     *
     * @param environment the application environment
     * @param configLoader mechanism for loading configuration
     * @throws ConfigurationException thrown if DoS configuration cannot be loaded.
     */
    private static void setUpDosFilter(final Environment environment, final ConfigLoader configLoader) throws ConfigurationException {
        final DenialOfServiceConfiguration dosConfig = configLoader.loadConfiguration(
            DenialOfServiceConfiguration.class);

        if (Boolean.TRUE.equals(dosConfig.getEnableFilter())) {
            final FilterRegistration.Dynamic dosFilter = environment.servlets()
                .addFilter("dosFilter", DoSFilter.class);
            dosFilter.setInitParameter(
                "maxRequestsPerSec",
                Long.toString(dosConfig.getMaxRequestsPerSec()));
            dosFilter.setInitParameter("maxRequestMs", Long.toString(dosConfig.getMaxRequestMs()));
            dosFilter.setInitParameter("maxWaitMs", Long.toString(dosConfig.getMaxWaitMs()));
            dosFilter.setInitParameter(
                "throttledRequests",
                Long.toString(dosConfig.getThrottledRequests()));
            dosFilter.setInitParameter("delayMs", Long.toString(dosConfig.getDelayMs()));
            dosFilter.setInitParameter("ipWhitelist", dosConfig.getIpWhitelist());
            dosFilter.setInitParameter("tooManyCode", dosConfig.getStatusCode());
            dosFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        }
    }

    /**
     * Set up CORS filter.
     *
     * @param configuration service application configuration
     * @param environment the application environment
     */
    private static void setUpCorsFilter(final MetaBooksConfiguration configuration, final Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets()
            .addFilter("cors", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", configuration.getAllowedOrigins());
        cors.setInitParameter("allowedHeaders", configuration.getAllowedHeaders());
        cors.setInitParameter("allowedMethods", configuration.getAllowedMethods());
        cors.setInitParameter("exposedHeaders", configuration.getExposedHeaders());
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private void configureBasicAuth(final AuthConfiguration configuration, final EncryptionConfiguration cipherConfiguration, final Environment environment) {
        if (null == databaseConnection) {
            LOGGER.error("{}Unable to connect to database, basic auth will not be available.{}",LogColour.ANSI_RED, LogColour.ANSI_RESET);
            return;
        }

        // For now, only basic auth is supported
        if ("basic".equalsIgnoreCase(configuration.getAuthType())) {
            environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<UserAuth>()
                    .setAuthenticator(new BasicAuthenticator(getUserDao(), cipherConfiguration))
                    .setAuthorizer(new BasicAuthorizer(getUserDao(), getRoleDao()))
                    .setRealm(configuration.getRealm())
                    .buildAuthFilter()));
        } else {
            LOGGER.warn("Auth type not specified, no authentication possible");
        }

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(UserAuth.class));
    }
}
