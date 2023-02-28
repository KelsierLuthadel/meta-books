/*
 * Copyright (c) 2023 Kelsier Luthadel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kelsier.bookshelf.framework.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;
import net.kelsier.bookshelf.framework.encryption.Cipher;
import net.kelsier.bookshelf.framework.encryption.EncryptedModule;
import net.kelsier.bookshelf.framework.validator.ObjectValidator;
import net.kelsier.bookshelf.framework.validator.ValidationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * Provides a mechanism to load YAML configuration files
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class YamlConfigLoader implements ConfigLoader {
    private final File configPath;
    private final Cipher cipher;

    /**
     * Constructor
     *
     * @param configPath The path to the directory containing the configuration files
     * @param cipher     A cipher to use when decrypting encrypted configuration properties
     */
    public YamlConfigLoader(final String configPath, final Cipher cipher) {
        this.configPath = new File(configPath);
        this.cipher = cipher;
    }

    /**
     * Load a specific configuration class. Attempts to read a YAML file (".yaml") with the same name as the class provided.
     *
     * @param configClass The class that represents the typed configuration.
     * @param <T>         The type of the configuration.
     * @return An instance of the specified class type populated with the configuration.
     */
    @Override
    public <T> T loadConfiguration(final Class<T> configClass) throws ConfigurationException {
        final File configFile = new File(configPath, configClass.getSimpleName() + ".yaml");
        try (final FileInputStream inputStream = new FileInputStream(configFile)){
            return readConfiguration(inputStream, configClass);
        } catch (final IOException e) {
            throw new ConfigurationException("Failed to load configuration file " + configFile.getPath(), e);
        }
    }

    /**
     * Takes an input stream containing configuration data and a class representing the expected structure of the configuration
     * Attempts to parse the input data into the expected configuration class and returns an instance.
     *
     * @param src         - Stream of configuration data
     * @param configClass - The expected configuration class
     * @param <T>         - The type of the configuration
     * @return An instance of the specified class type populated with the configuration.
     * @throws ConfigurationException if the configuration cannot be loaded.
     */
    private <T> T readConfiguration(final InputStream src, final Class<T> configClass) throws ConfigurationException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        //Decrypt encrypted fields using the custom encrypted module
        if (null != cipher) {
            mapper.registerModule(new EncryptedModule(cipher));
        }

        try {
            //Read config
            final T config = mapper.readValue(src, configClass);

            //Check for constraint violations
            ObjectValidator.validateMapping(config);

            return config;
        } catch (final ValidationException | IOException e) {
            final String message = MessageFormat.format("{0} failed due to: {1}", configClass.getSimpleName(), e.getMessage());
            throw new ConfigurationException(message, e);
        }
    }
}
