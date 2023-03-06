package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CustomColumnLinkTest {
    @Test
    void testValid() {
        final CustomColumnLink link = new CustomColumnLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final CustomColumnLink link = new CustomColumnLink(null, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final CustomColumnLink link = new CustomColumnLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final CustomColumnLink link = new CustomColumnLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final CustomColumnLink link = new CustomColumnLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullValue() {
        final CustomColumnLink link = new CustomColumnLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroLink() {
        final CustomColumnLink link = new CustomColumnLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}