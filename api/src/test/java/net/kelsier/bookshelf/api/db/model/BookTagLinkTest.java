package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.links.BookTagLink;
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

        zeroIdTest(bookTagLink, "must not be null");
    }

    @Test
    void testZeroId() {
        final BookTagLink bookTagLink = new BookTagLink(0, 1, 1);

        zeroIdTest(bookTagLink, "must be greater than or equal to 1");
    }

    private void zeroIdTest(final BookTagLink bookTagLink, final String expected) {
        final Set<ConstraintViolation<Object>> violations = validate(bookTagLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals(expected, authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookTagLink bookTagLink = new BookTagLink(1, null, 1);

        zeroIdTest(bookTagLink, "must not be null");
    }

    @Test
    void testZeroBook() {
        final BookTagLink bookTagLink = new BookTagLink(1, 0, 1);

        zeroIdTest(bookTagLink, "must be greater than or equal to 1");
    }

    @Test
    void testNullTag() {
        final BookTagLink bookTagLink = new BookTagLink(1, 1, null);

        zeroIdTest(bookTagLink, "must not be null");
    }

    @Test
    void testZeroTag() {
        final BookTagLink bookTagLink = new BookTagLink(1, 1, 0);

        zeroIdTest(bookTagLink, "must be greater than or equal to 1");
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}