package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookAuthorLinkTest {
    @Test
    void testValid() {
        final BookAuthorLink bookAuthorLink =  new BookAuthorLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookAuthorLink bookAuthorLink =  new BookAuthorLink(null, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookAuthorLink bookAuthorLink =  new BookAuthorLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullAuthor() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroAuthor() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}