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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import net.kelsier.bookshelf.framework.error.response.ExceptionToResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

// JsonProcessingException is thrown by Jackson when a non-IO problem is encountered while processing JSON

/**
 * Map JSON errors to Response objects
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
    private static final int UNPROCESSABLE_ENTITY = 422;

    /**
     * Transform JsonProcessingException into a valid HTTP Response
     *
     * @param exception the thrown exception
     * @return HTTP Response
     */
    @Override
    public final Response toResponse(final JsonProcessingException exception) {
        final String prefix;
        final int status;

        if (exception instanceof UnrecognizedPropertyException) {
            prefix = String.format("Unrecognized property '%s'", ((UnrecognizedPropertyException) exception).getPropertyName());
            status = UNPROCESSABLE_ENTITY;
        } else if (exception instanceof JsonParseException) {
            // JsonParseException is thrown when the request JSON is not-well-formed
            prefix = "Invalid JSON in request";
            status = Response.Status.BAD_REQUEST.getStatusCode();
        } else {
            prefix = Response.Status.BAD_REQUEST.getReasonPhrase();
            status = Response.Status.BAD_REQUEST.getStatusCode();
        }

        return ExceptionToResponse.makeResponse(
            status,
            UUID.randomUUID().toString(),
            getMessage(prefix, exception),
            exception,
            null,
            null
        );
    }

    /**
     * Get the reason for a thrown exception
     *
     * @param prefix    a string to prefix to the returned message
     * @param exception the thrown exception
     * @return The message as a string
     */
    private String getMessage(final String prefix, final JsonProcessingException exception) {
        final StringBuilder message = new StringBuilder(prefix);

        if (exception.getLocation() != null) {
            message
                .append(" at line ").append(exception.getLocation().getLineNr())
                .append(", ")
                .append("column ").append(exception.getLocation().getColumnNr());
        }

        return message.toString();
    }
}
