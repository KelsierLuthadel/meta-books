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

package net.kelsier.bookshelf.framework.error.exception;

import net.kelsier.bookshelf.framework.error.response.ExceptionToResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

/**
 * Transform a WebApplicationSilentException into a valid HTTP Response
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Provider
public class WebApplicationSilentExceptionMapper implements ExceptionMapper<WebApplicationSilentException> {
    /**
     * Transform a WebApplicationSilentException into a valid HTTP Response
     *
     * @param exception the thrown exception
     * @return HTTP Response
     */
    @Override
    public final Response toResponse(final WebApplicationSilentException exception) {
        final String responseUuid = UUID.randomUUID().toString();
        final int responseStatus = exception.getResponse().getStatus();

        final String message = exception.getMessage();
        return ExceptionToResponse.makeResponse(responseStatus, responseUuid, message, exception, null, null);
    }
}
