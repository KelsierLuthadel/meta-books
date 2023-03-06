package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CustomColumnTest {

    @Test
    void testValid() {
        final CustomColumn customColumn = new CustomColumn(1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(customColumn);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final CustomColumn customColumn = new CustomColumn(null, "text");

        final Set<ConstraintViolation<Object>> violations = validate(customColumn);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final CustomColumn customColumn = new CustomColumn(0, "text");

        final Set<ConstraintViolation<Object>> violations = validate(customColumn);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullValue() {
        final CustomColumn customColumn = new CustomColumn(1, null);

        final Set<ConstraintViolation<Object>> violations = validate(customColumn);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}