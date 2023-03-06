package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LanguageTest {
    @Test
    void testValid() {
        final Language language = new Language(1, "lang");
        final Set<ConstraintViolation<Object>> violations = validate(language);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Language language = new Language(null, "lang");
        final Set<ConstraintViolation<Object>> violations = validate(language);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Language language = new Language(0, "lang");
        final Set<ConstraintViolation<Object>> violations = validate(language);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullCode() {
        final Language language = new Language(1, null);
        final Set<ConstraintViolation<Object>> violations = validate(language);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}