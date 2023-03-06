package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookTagLinkTest {
    @Test
    void testValid() {
        final BookTagLink bookTagLink = new BookTagLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookTagLink bookTagLink = new BookTagLink(null, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookTagLink bookTagLink = new BookTagLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookTagLink bookTagLink = new BookTagLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final BookTagLink bookTagLink = new BookTagLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullTag() {
        final BookTagLink bookTagLink = new BookTagLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroTag() {
        final BookTagLink bookTagLink = new BookTagLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}