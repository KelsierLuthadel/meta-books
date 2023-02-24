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

package net.kelsier.bookshelf.framework.validator;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

/**
 * Thrown when validation violations are found during the object validation
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings({"rawtypes", "unused"})

public final class ValidationException extends Exception {
    private final transient HashSet<ConstraintViolation> constraintViolations;

    /**
     * Constructor
     *
     * @param message              the message indicating the problem
     * @param constraintViolations {@code Set} of {@link ConstraintViolation}.
     */
    ValidationException(final String message,
                        final Set<? extends ConstraintViolation> constraintViolations) {
        super(message);
        this.constraintViolations = (constraintViolations == null) ? new HashSet<>() : new HashSet<>(constraintViolations);
    }

    /**
     * Set of constraint violations reported during a validation.
     *
     * @return {@code Set} of {@link ConstraintViolation}
     */
    public Set<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }
}

