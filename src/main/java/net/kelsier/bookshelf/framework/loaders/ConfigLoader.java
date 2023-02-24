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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kelsier.bookshelf.framework.loaders;

import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;

/**
 * Provides a mechanism to load configuration.
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@FunctionalInterface
public interface ConfigLoader {

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
