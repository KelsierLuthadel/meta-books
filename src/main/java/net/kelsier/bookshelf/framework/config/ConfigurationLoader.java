package net.kelsier.bookshelf.framework.config;

import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;

/**
 * Provides a mechanism to load configuration.
 */
@FunctionalInterface
public interface ConfigurationLoader {

    /**
     * Load a specific configuration class.
     *
     * @param configClass The class that represents the typed configuration.
     * @param <T>         The type of the configuration.
     * @return An instance of the specified class type populated with the configuration.
     * @throws ConfigurationException if the configuration cannot be loaded.
     */
    <T> T loadConfiguration(Class<T> configClass) throws ConfigurationException;
}
