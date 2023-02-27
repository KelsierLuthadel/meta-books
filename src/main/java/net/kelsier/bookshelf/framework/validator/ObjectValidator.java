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

package net.kelsier.bookshelf.framework.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.MessageFormat;
import java.util.Set;

/**
 * This class provides a means of validating objects against their validation annotations
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class ObjectValidator {
    /**
     * A utility class, shouldn't be instantiated.
     */
    private ObjectValidator() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }

    /**
     * Take an object and validate it. If any violations are found, throw an exception
     *
     * @param obj The object to validate
     * @param <T> The type of the given object
     * @return validated object
     * @throws ValidationException - Thrown if the object is not valid
     */
    public static <T> T validateMapping(final T obj) throws ValidationException {
        Validator validator = Validation.byDefaultProvider()
                .configure()
                .buildValidatorFactory()
                .getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate( obj );

        if (violations.isEmpty()) {
            return obj;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Violations: ");

        violations.forEach(violation ->
                stringBuilder.append(MessageFormat.format("\nViolation: {0}:{1}", violation.getPropertyPath(), violation.getMessage())));

        throw new ValidationException(stringBuilder.toString(), violations);
    }


}
