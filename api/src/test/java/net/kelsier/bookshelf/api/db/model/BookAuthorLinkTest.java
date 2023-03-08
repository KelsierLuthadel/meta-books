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
        testZeroParameter(new BookAuthorLink(null, 1, 1), "must not be null");
    }

    @Test
    void testZeroId() {
        testZeroParameter(new BookAuthorLink(0, 1, 1), "must be greater than or equal to 1");
    }

    private void testZeroParameter(final BookAuthorLink bookAuthorLink, final String expected) {
        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals(expected, authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        testZeroParameter(new BookAuthorLink(1, null, 1), "must not be null");
    }

    @Test
    void testZeroBook() {
        testZeroParameter(new BookAuthorLink(1, 0, 1), "must be greater than or equal to 1");
    }

    @Test
    void testNullAuthor() {
        testZeroParameter(new BookAuthorLink(1, 1, null), "must not be null");
    }

    @Test
    void testZeroAuthor() {
        testZeroParameter(new BookAuthorLink(1, 1, 0), "must be greater than or equal to 1");
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}