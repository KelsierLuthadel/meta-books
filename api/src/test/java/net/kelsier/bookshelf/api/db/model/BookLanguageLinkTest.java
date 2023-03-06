package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
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

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLanguage() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroLanguage() {
        final BookLanguageLink bookLanguageLink = new BookLanguageLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(bookLanguageLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }


    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}