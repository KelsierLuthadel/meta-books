package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookPublisherLinkTest {
    @Test
    void testValid() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(null, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPublisher() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroPublisher() {
        final BookPublisherLink bookPublisherLink = new BookPublisherLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(bookPublisherLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }


    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}