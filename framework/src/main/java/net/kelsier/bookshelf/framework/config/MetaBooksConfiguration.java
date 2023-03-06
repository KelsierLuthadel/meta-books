package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.pac4j.dropwizard.Pac4jFactory;

import javax.validation.Valid;

public class MetaBooksConfiguration extends Configuration {
    @Valid
    private final Pac4jFactory pac4jFactory = new Pac4jFactory();

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

    @JsonSetter(value = "databaseEnabled", nulls = Nulls.SKIP)
    private final Boolean databaseEnabled;

    @JsonSetter(value = "cipherPass", nulls = Nulls.SKIP)
    private final String cipherPass;


    /**
     * @param allowedOrigins                    CORS allowed origins
     * @param allowedHeaders                    CORS allowed headers
     * @param allowedMethods                    CORS allowed methods
     * @param exposedHeaders                    CORS exposed headers
     * @param configPath                        path for configuration files
     * @param databaseEnabled                   Enable database
     * @param cipherPass                        Cipher password. It is recommended to set this as a system property
     */
    @JsonCreator
    public MetaBooksConfiguration(@JsonProperty("allowedOrigins") final String allowedOrigins,
                                  @JsonProperty("allowedHeaders") final String allowedHeaders,
                                  @JsonProperty("allowedMethods") final String allowedMethods,
                                  @JsonProperty("exposedHeaders") final String exposedHeaders,
                                  @JsonProperty("configPath") final String configPath,
                                  @JsonProperty("databaseEnabled") final Boolean databaseEnabled,
                                  @JsonProperty("cipherPass") final String cipherPass) {
        this.allowedOrigins = allowedOrigins;
        this.allowedHeaders = allowedHeaders;
        this.allowedMethods = allowedMethods;
        this.exposedHeaders = exposedHeaders;
        this.configPath = configPath;
        this.databaseEnabled = databaseEnabled;
        this.cipherPass = cipherPass;
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

    /**
     *  Return true if the database will be configured. If this is false, then functionality
     *  will be restricted.
     *
     * @return true if the database will be configured
     */
    public boolean isDatabaseEnabled() {
        return databaseEnabled;
    }

    /**
     * Cipher password. It is recommended to set this as a system property
     *
     * @return Cipher password
     */
    public String getCipherPass() {
        return cipherPass;
    }
}
