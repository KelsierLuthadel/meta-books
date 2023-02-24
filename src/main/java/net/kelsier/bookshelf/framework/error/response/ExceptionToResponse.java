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

import net.kelsier.bookshelf.framework.error.APIResponseError;
import net.kelsier.bookshelf.framework.error.exception.WebApplicationSilentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Map exception messages to a Response message to be sent back with a response
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class ExceptionToResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionToResponse.class);

    /**
     * Throw an exception if something tries to instantiate this class
     */
    private ExceptionToResponse() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }

    /**
     * Transform an Exception into a custom HTTP response that is required to be returned by the framework
     *
     * @param status    HTTP Response Code
     * @param uuid      a UUID used to track the exception in log files
     * @param message   the message as a string
     * @param exception the thrown exception
     * @param errorCode an optional error code
     * @param helpUrl   an optional help URL that describes the original calling API
     * @param errors    an optional list of errors that relates to reason for the exception
     * @return an HTTP Response
     */
    public static Response makeResponse(final int status,
                                        final String uuid,
                                        final String message,
                                        final Throwable exception,
                                        final Integer errorCode,
                                        final String helpUrl,
                                        final List<APIResponseError> errors) {
        final ResponseErrorFormat responseErrorFormat = new ResponseErrorFormat(
                status,
                uuid,
                message,
                errorCode,
                helpUrl,
                errors
        );

        final StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        // outputs a debug message for WebApplicationSilentExceptions with 401 or 403 error codes. Anything else gets an error message
        if ((exception.getClass() == WebApplicationSilentException.class) && ((status == 401) || (status == 403))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Debug: id: {}. {}", uuid, sw);
            }
        } else {
            if (LOGGER.isErrorEnabled() && status != 404) {
                LOGGER.error("Error: id: {}. {}", uuid, sw);
            }
        }

        return Response
            .status(status)
            .entity(responseErrorFormat)
            .type(Utf8MediaType.APPLICATION_JSON)
            .build();
    }

    /**
     * Transform an Exception into a custom HTTP response that is required to be returned by the framework
     *
     * @param status    HTTP Response Code
     * @param uuid      a UUID used to track the exception in log files
     * @param message   the message as a string
     * @param exception the thrown exception
     * @param helpUrl   an optional help URL that describes the original calling API
     * @param errors    an optional list of errors that relates to reason for the exception
     * @return an HTTP Response
     */
    public static Response makeResponse(final int status,
                                        final String uuid,
                                        final String message,
                                        final Throwable exception,
                                        final String helpUrl,
                                        final List<APIResponseError> errors) {
        return makeResponse(status, uuid, message, exception, null, helpUrl, errors);
    }
}
