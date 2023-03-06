package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookDataTest {
    @Test
    void testValid() {
        final BookData bookData = new BookData(1, 1, "text", 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(bookData);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookData bookData = new BookData(null, 1, "text", 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(bookData);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookData bookData = new BookData(0, 1, "text", 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(bookData);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullFormat() {
        final BookData bookData = new BookData(1, 1, null, 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(bookData);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSize() {
        final BookData bookData = new BookData(1, 1, "text", null, "name");
        final Set<ConstraintViolation<Object>> violations = validate(bookData);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullName() {
        final BookData bookData = new BookData(1, 1, "text", 1, null);
        final Set<ConstraintViolation<Object>> violations = validate(bookData);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}