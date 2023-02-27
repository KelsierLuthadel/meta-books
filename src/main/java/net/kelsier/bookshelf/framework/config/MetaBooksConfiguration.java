package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.pac4j.dropwizard.Pac4jFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MetaBooksConfiguration extends Configuration {
    @Valid
    private final Pac4jFactory pac4jFactory = new Pac4jFactory();

    /**
     * Create a datasource
     * Database factory
     */
    @Valid
    @NotNull
    @JsonProperty("database")
    private final DataSourceFactory database = new DataSourceFactory();

    @JsonSetter(value = "allowedOrigins", nulls = Nulls.SKIP)
    private final String allowedOrigins;

    @JsonSetter(value = "allowedHeaders", nulls = Nulls.SKIP)
    private final String allowedHeaders;

    @JsonSetter(value = "allowedMethods", nulls = Nulls.SKIP)
    private final String allowedMethods;

    @JsonSetter(value = "exposedHeaders", nulls = Nulls.SKIP)
    private final String exposedHeaders;

    @JsonSetter(value = "configPath", nulls = Nulls.SKIP)
    private final String configPath;

    /**
     * @param allowedOrigins                    CORS allowed origins
     * @param allowedHeaders                    CORS allowed headers
     * @param allowedMethods                    CORS allowed methods
     * @param exposedHeaders                    CORS exposed headers
     * @param configPath                        path for configuration files
     */
    @JsonCreator
    public MetaBooksConfiguration(@JsonProperty("allowedOrigins") final String allowedOrigins,
                                  @JsonProperty("allowedHeaders") final String allowedHeaders,
                                  @JsonProperty("allowedMethods") final String allowedMethods,
                                  @JsonProperty("exposedHeaders") final String exposedHeaders,
                                  @JsonProperty("configPath") final String configPath) {
        this.allowedOrigins = allowedOrigins;
        this.allowedHeaders = allowedHeaders;
        this.allowedMethods = allowedMethods;
        this.exposedHeaders = exposedHeaders;
        this.configPath = configPath;
    }

    /**
     * Get the PAC4J factory
     *
     * @return pac4j factory
     */
    @JsonProperty("pac4j")
    public Pac4jFactory getPac4jFactory() {
        return pac4jFactory;
    }



    /**
     * Get the datasource
     *
     * @return A datasource factory
     */
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    /**
     * Return the CORS allowed origins
     *
     * @return CORS allowed origins
     */
    public String getAllowedOrigins() { return allowedOrigins; }

    /**
     *  Return the CORS allowed headers
     *
     * @return CORS allowed headers
     */
    public String getAllowedHeaders() { return allowedHeaders; }

    /**
     *  Return the CORS allowed methods
     *
     * @return CORS allowed methods
     */
    public String getAllowedMethods() { return allowedMethods; }

    /**
     *  Return the CORS exposed headers
     *
     * @return CORS exposed headers
     */
    public String getExposedHeaders() { return exposedHeaders; }

    /**
     *  Return the path used to store extra config files
     *
     * @return path for configuration files
     */
    public String getConfigPath() { return configPath; }
}
