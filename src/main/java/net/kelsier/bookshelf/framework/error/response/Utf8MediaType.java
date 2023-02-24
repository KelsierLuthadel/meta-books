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

package net.kelsier.bookshelf.framework.error.response;

import javax.ws.rs.core.MediaType;

/**
 * Exposes {@link MediaType} types with the utf-8 charset applied
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings("unused")
public final class Utf8MediaType {
    private static final String CHARSET = ";charset=utf-8";

    /**
     * Applies the utf-8 charset to the application/json media type
     */
    public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + CHARSET;

    /**
     * Applies the utf-8 charset to the text/html media type
     */
    public static final String TEXT_HTML = MediaType.TEXT_HTML + CHARSET;

    /**
     * This class should not be instantiated.
     */
    private Utf8MediaType() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }
}