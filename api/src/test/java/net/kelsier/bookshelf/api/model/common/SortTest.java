package net.kelsier.bookshelf.api.model.common;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class SortTest {
    @Test
    void testValidField() {
        assertEquals(0, validate(new Sort("field", "asc")).size(),
                "The name field should be valid");
    }

    @Test
    void testValidDirection() {
        assertEquals(0, validate(new Sort("field", "asc")).size(),
                "The name field should be valid");
        assertEquals(0, validate(new Sort("field", "desc")).size(),
                "The name field should be valid");
    }

    @Test
    void testInvalidField() {
        final Sort sort = new Sort(null, "asc");
        final Set<ConstraintViolation<Object>> violations = validate(sort);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testInvalidDirection() {
        final Sort sort = new Sort("name", "none");
        final Set<ConstraintViolation<Object>> violations = validate(sort);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [asc, desc]", violation.getMessage());
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}