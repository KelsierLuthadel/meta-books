package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    @Test
    void testValidAuthor() {
        final Author author = new Author(1, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(author);
        assertEquals(0, violations.size(), "There should be no violations");
    }

    @Test
    void testNullId() {
        final Author author = new Author(null, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(author);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinimumFailId() {
        final Author author = new Author(0, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(author);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals(
            "must be greater than or equal to 1",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullName() {
        final Author author = new Author(1, null, "sort");
        final Set<ConstraintViolation<Object>> violations = validate(author);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}