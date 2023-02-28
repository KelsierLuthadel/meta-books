package net.kelsier.bookshelf.framework.validator;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

/**
 * Thrown when validation violations are found during the validation of an object
 */
public final class CheckedValidationException extends Exception {
    private final HashSet<ConstraintViolation> constraintViolations;

    /**
     * Create a new CheckedValidationException.
     *
     * @param message              the message indicating the problem
     * @param constraintViolations {@code Set} of {@link ConstraintViolation}. Empty set made if null passed.
     */
    CheckedValidationException(final String message,
                               final Set<? extends ConstraintViolation> constraintViolations) {
        super(message);
        this.constraintViolations = (constraintViolations == null) ? new HashSet<>() : new HashSet<>(constraintViolations);
    }

    /**
     * Set of constraint violations reported during a validation.
     *
     * @return {@code Set} of {@link ConstraintViolation}
     */
    public final Set<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }
}

