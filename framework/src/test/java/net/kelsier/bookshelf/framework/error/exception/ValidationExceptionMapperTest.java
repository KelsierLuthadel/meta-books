/*
 * Copyright (c) Kelsier Luthadel 2023.
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
 *
 */

package net.kelsier.bookshelf.framework.error.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dropwizard.jersey.validation.JerseyViolationException;
import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.framework.error.response.ResponseErrorFormat;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import java.util.Set;

import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionMapperTest {

    @Test
    void testMapperWithNull() {
        class NUllConstraint {
            @NotNull final String notNull;
            NUllConstraint(final String notNull) {this.notNull = notNull;}
        }

        final Set<ConstraintViolation<NUllConstraint>> validation = Validators.newValidator().validate(new NUllConstraint(null));
        ValidationExceptionMapper m = new ValidationExceptionMapper();

        try (Response response = m.toResponse(new JerseyViolationException(validation, null))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), response.getStatus());
            ResponseErrorFormat format = (ResponseErrorFormat)response.getEntity();
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), format.getStatus());
            assertEquals("Validation error", format.getMessage());
            assertEquals(1, format.getErrors().size());
            assertEquals("must not be null", format.getErrors().get(0).getDetail());
            assertEquals("notNull", format.getErrors().get(0).getCause());
        }
    }

    @Test
    void testMapperWithLimits() {
        class NUllConstraint {
            @Min(1024) final Integer minimum;
            NUllConstraint(final Integer notNull) {this.minimum = notNull;}
        }

        final Set<ConstraintViolation<NUllConstraint>> validation = Validators.newValidator().validate(new NUllConstraint(1023));
        ValidationExceptionMapper m = new ValidationExceptionMapper();

        try (Response response = m.toResponse(new JerseyViolationException(validation, null))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), response.getStatus());
            ResponseErrorFormat format = (ResponseErrorFormat)response.getEntity();
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), format.getStatus());
            assertEquals("Validation error", format.getMessage());
            assertEquals(1, format.getErrors().size());
            assertEquals("must be greater than or equal to 1024", format.getErrors().get(0).getDetail());
            assertEquals("1023", format.getErrors().get(0).getCause());
        }
    }

    @Test
    void testMapperWithMultiple() {
        @JsonPropertyOrder({"min", "max", "total"})
        class Multiple {
            @Min(1024) final Integer min;
            @Max(1024) final Integer max;
            @NotNull final Integer total;
            Multiple(Integer notNull, Integer max, Integer total) { this.min = notNull; this.max = max; this.total = total; }
        }

        final Set<ConstraintViolation<Multiple>> validation = Validators.newValidator().validate(new Multiple(1023, 1025, null));
        ValidationExceptionMapper m = new ValidationExceptionMapper();

        try (Response response = m.toResponse(new JerseyViolationException(validation, null))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), response.getStatus());
            ResponseErrorFormat format = (ResponseErrorFormat)response.getEntity();
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), format.getStatus());
            assertEquals("Validation error", format.getMessage());
            assertEquals(3, format.getErrors().size());
        }
    }

}