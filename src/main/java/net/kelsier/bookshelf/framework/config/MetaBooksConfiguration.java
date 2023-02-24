package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.pac4j.dropwizard.Pac4jFactory;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class MetaBooksConfiguration extends Configuration {
    private Pac4jFactory pac4jFactory = new Pac4jFactory();
    @JsonSetter(value = "allowedOrigins", nulls = Nulls.SKIP)
    private String allowedOrigins = "*";

    @JsonSetter(value = "allowedHeaders", nulls = Nulls.SKIP)
    private String allowedHeaders = "X-Requested-With,Content-Type,Accept,Origin,Authorization,Cache-Control,Content-Disposition";

    @JsonSetter(value = "allowedMethods", nulls = Nulls.SKIP)
    private String allowedMethods = "OPTIONS,GET,PUT,POST,DELETE,HEAD";

    @JsonSetter(value = "exposedHeaders", nulls = Nulls.SKIP)
    private String exposedHeaders = "Content-Disposition";

    @JsonSetter(value = "configPath", nulls = Nulls.SKIP)
    private String configPath = "./config";

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
     * Save a PAC4J object, this is provided and called by the framework
     *
     * @param pac4jFactory Saves a pac4j object
     */
    @JsonProperty("pac4j")
    private void setPac4jFactory(Pac4jFactory pac4jFactory) {
        this.pac4jFactory = pac4jFactory;
    }

    /**
     * Create a datasource
     * Database factory
     */
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    /**
     * Set a new datasource
     *
     * @param factory A factory to save
     */
    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
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
