package net.kelsier.bookshelf;

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
import net.kelsier.bookshelf.api.resource.Bookshelf;
import net.kelsier.bookshelf.api.resource.Login;
import net.kelsier.bookshelf.api.resource.RoleAdministration;
import net.kelsier.bookshelf.api.resource.UserAdministration;
import net.kelsier.bookshelf.framework.MetaBooksInfo;
import net.kelsier.bookshelf.framework.auth.*;
import net.kelsier.bookshelf.framework.config.DenialOfServiceConfiguration;
import net.kelsier.bookshelf.framework.config.MetaBooksConfiguration;
import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;
import net.kelsier.bookshelf.framework.db.dao.RoleDAO;
import net.kelsier.bookshelf.framework.db.dao.UserDAO;
import net.kelsier.bookshelf.framework.encryption.JasyptCipher;
import net.kelsier.bookshelf.framework.environment.ResourceRegistrar;
import net.kelsier.bookshelf.framework.error.exception.JsonProcessingExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.ResponseErrorExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.RuntimeExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.StartupException;
import net.kelsier.bookshelf.framework.error.exception.TechnicalExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.ValidationExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.WebApplicationExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.WebApplicationSilentExceptionMapper;
import net.kelsier.bookshelf.framework.filter.CacheControlFilter;
import net.kelsier.bookshelf.framework.health.DatabaseHealth;
import net.kelsier.bookshelf.framework.loaders.ConfigLoader;
import net.kelsier.bookshelf.framework.loaders.YamlConfigLoader;
import net.kelsier.bookshelf.framework.log.LogColour;
import net.kelsier.bookshelf.framework.openapi.OpenApi;
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
                return configuration.getDataSourceFactory();
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

        final JasyptCipher cipher = getJasyptCipher();

        // Configuration loader for yaml -> pojo
        final ConfigLoader configLoader = new YamlConfigLoader(
            configuration.getConfigPath(),
            cipher
        );

        // Register custom Exception Mappers
        registerExceptionMappers();

        // Disable caching
        setCacheHeaders(environment);

        // Create CORS and DOS Filter
        setupFilters(configuration, environment, configLoader);


        databaseConnection = getJdbiFactory(configuration, environment);

        // Add tasks to the environment
        addToEnvironment();

        // Configure basic authentication
        configureBasicAuth(environment);

        // Register resources
        registerOpenAPI(environment);
        registerRestResources();

        // Register health checks
        registerHealthChecks(environment, getRoleDao());

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

    private static JasyptCipher getJasyptCipher() {
        if (null != System.getProperty("CIPHER")) {
            return new JasyptCipher(System.getProperty("CIPHER"));
        }

        return null;
    }

    private Jdbi getJdbiFactory(MetaBooksConfiguration configuration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        return factory.build(
                environment,
                configuration.getDataSourceFactory(),
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
        resourceRegistrar.registerResource(new JsonProcessingExceptionMapper());
    }

    private void addToEnvironment() {
        //Enable multipart form data
        resourceRegistrar.registerResource(MultiPartFeature.class);
    }

    private static void setupFilters(MetaBooksConfiguration configuration, Environment environment, ConfigLoader configLoader) throws ConfigurationException {
        // Setup CORS filter
        setUpCorsFilter(configuration, environment);

        // Setup DoS filter
        setUpDosFilter(environment, configLoader);
    }

    private void registerOpenAPI(final Environment environment) {
        OpenApi.configure(environment, "/api", Stream.of("net.kelsier.bookshelf.api.resource").collect(
            Collectors.toSet()));

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

    private void registerRestResources() {
     resourceRegistrar.registerResource(new Login(getUserDao()));
     resourceRegistrar.registerResource(new UserAdministration(getUserDao()));
     resourceRegistrar.registerResource(new RoleAdministration(getRoleDao()));
     resourceRegistrar.registerResource(new Bookshelf());
    }

    private void registerHealthChecks(final Environment environment, final RoleDAO roleDAO) {
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

    /**
     * Set up DoS filter.
     *
     * @param environment - the application environment
     * @param configLoader - mechanism for loading configuration
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
     * @param configuration - service application configuration
     * @param environment - the application environment
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

    private void configureBasicAuth(final Environment environment) {
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<UserAuth>()
                        .setAuthenticator(new BasicAuthenticator(getUserDao()))
                        .setAuthorizer(new BasicAuthorizer(getUserDao(), getRoleDao()))
                        .setRealm("realm")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(UserAuth.class));
    }
}
