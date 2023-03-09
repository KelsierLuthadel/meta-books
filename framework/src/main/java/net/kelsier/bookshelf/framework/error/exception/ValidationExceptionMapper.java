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

import io.dropwizard.jersey.validation.ConstraintMessage;
import io.dropwizard.jersey.validation.JerseyViolationException;
import net.kelsier.bookshelf.framework.error.APIResponseError;
import net.kelsier.bookshelf.framework.error.response.ExceptionToResponse;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Transform a Jersey Violation Exception into a valid HTTP Response
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<JerseyViolationException> {
    /**
     * Transform a Jersey Violation Exception into a valid HTTP Response
     * @param exception the thrown exception
     * @return HTTP Response
     */
    @Override
    public final Response toResponse(final JerseyViolationException exception) { final String responseUuid = UUID.randomUUID().toString();
        final int responseStatus = ConstraintMessage.determineStatus(exception.getConstraintViolations(), exception.getInvocable());

        final List<APIResponseError> apiResponseErrors = new LinkedList<>();
        exception.getConstraintViolations().forEach((ConstraintViolation<?> constraintViolation) -> {
            final String cause;
            if (null != constraintViolation.getInvalidValue()) {
                cause = constraintViolation.getInvalidValue().toString();
            } else {
                cause = constraintViolation.getPropertyPath().toString();
            }

            apiResponseErrors.add(new APIResponseError(constraintViolation.getMessage(), cause));
        });

        return ExceptionToResponse.makeResponse(responseStatus, responseUuid, "Validation error", exception, null, apiResponseErrors);
    }
}
