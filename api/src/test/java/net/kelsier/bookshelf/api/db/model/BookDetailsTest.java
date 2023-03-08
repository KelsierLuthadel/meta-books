package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookDetailsTest {

    @Test
    void testValidAuthor() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(0, violations.size(), "There should be no violations");
    }

    @Test
    void testNullId() {
        final BookDetails bookDetails = new BookDetails(null, "title", "author", "series",
                1, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinimumFailId() {
        final BookDetails bookDetails = new BookDetails(0, "title", "author", "series",
                1, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals(
            "must be greater than or equal to 1",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullTitle() {
        final BookDetails bookDetails = new BookDetails(1, null, "author", "series",
                1, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullAuthor() {
        final BookDetails bookDetails = new BookDetails(1, "title", null, "series",
                1, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeries() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", null,
                1, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeriesIndex() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                null, "publish", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPublisher() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, null, "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinIsbn() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "123", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("length must be between 10 and 17", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMaxIsbn() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "123456789012345678", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("length must be between 10 and 17", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLanguage() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", null, "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullFormat() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language", null, 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSize() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language", "format", null, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinSize() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language", "format", -1, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 0", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLastModified() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), null, "path", "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPath() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), null, "comments");

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullComments() {
        final BookDetails bookDetails = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language", "format", 100, true,
                new Timestamp(0), new Timestamp(0), new Timestamp(0), "path", null);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}