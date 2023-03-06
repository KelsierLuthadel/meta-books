package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CommentTest {
    @Test
    void testValid() {
        final Comment comment = new Comment(1, 1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Comment comment = new Comment(null, 1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Comment comment = new Comment(0, 1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final Comment comment = new Comment(1, null, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final Comment comment = new Comment(1, 0, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullComment() {
        final Comment comment = new Comment(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}