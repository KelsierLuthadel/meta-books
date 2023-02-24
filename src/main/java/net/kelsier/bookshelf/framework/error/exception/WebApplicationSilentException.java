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

package net.kelsier.bookshelf.framework.error.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * WebApplicationSilentException is based on WebApplicationException. It behaves the same way, but is used for filtering
 * out unwanted error output in ExceptionToResponse
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public class WebApplicationSilentException extends WebApplicationException {

    /**
     * @param response response object
     */
    public WebApplicationSilentException(final Response response) {
        super(response);
    }

    /**
     * @param status the status number for the response
     */
    public WebApplicationSilentException(final int status) {
        super(status);
    }
}