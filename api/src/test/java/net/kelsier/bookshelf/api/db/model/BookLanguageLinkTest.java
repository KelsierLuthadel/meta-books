package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.links.BookLanguageLink;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookLanguageLinkTest {
    @Test
    void testValid() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(null, 1, 1);

        zeroIdTests(bookLanguageLink, "must not be null");
    }

    @Test
    void testZeroId() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(0, 1, 1);

        zeroIdTests(bookLanguageLink, "must be greater than or equal to 1");
    }

    private void zeroIdTests(final BookLanguageLink bookLanguageLink, final String expected) {
        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals(expected, authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, null, 1);

        zeroIdTests(bookLanguageLink, "must not be null");
    }

    @Test
    void testZeroBook() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 0, 1);

        zeroIdTests(bookLanguageLink, "must be greater than or equal to 1");
    }

    @Test
    void testNullLanguage() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 1, null);

        zeroIdTests(bookLanguageLink, "must not be null");
    }

    @Test
    void testZeroLanguage() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 1, 0);

        zeroIdTests(bookLanguageLink, "must be greater than or equal to 1");
    }


    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}