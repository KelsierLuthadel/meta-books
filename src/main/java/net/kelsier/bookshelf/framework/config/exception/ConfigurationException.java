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

package net.kelsier.bookshelf.framework.config.exception;


/**
 * ConfigurationException
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class ConfigurationException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message the message that details the failure.
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message the message that details the failure.
     * @param cause   the underlying cause of the failure.
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
