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

package net.kelsier.bookshelf.framework.environment;

import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.model.Resource;

/**
 * A registrar for API resource registration to Jersey
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public class ResourceRegistrar {
    private final Environment environment;

    /**
     * Constructor
     *
     * @param environment - The Dropwizard environment onto which resources should be registered
     */
    public ResourceRegistrar(final Environment environment) { this.environment = environment; }

    /**
     * Registers the passed object onto the Dropwizard environment
     *
     * @param resource - A Jersey resource class
     */
    public void registerResource(final Resource resource) { environment.jersey().getResourceConfig().registerResources(resource); }

    /**
     * Registers the passed object onto the Dropwizard environment
     *
     * @param resource - A Jersey resource class
     */
    public void registerResource(final Object resource) {
        environment.jersey().register(resource);
    }
}
