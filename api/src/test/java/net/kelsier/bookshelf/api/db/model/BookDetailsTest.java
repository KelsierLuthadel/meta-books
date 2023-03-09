package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

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

    @Test
    void testEmptyFilePath() {
        final BookDetails bookDetails = createBookDetailsForPath("");
        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("invalid path format", authorConstraintViolation.getMessage()));
    }

    @Test
    void testFilePathManipulation() {
        final List<String> attemptedPathManipulation = new ArrayList<>();
        attemptedPathManipulation.add("../path");
        attemptedPathManipulation.add("../path/path");
        attemptedPathManipulation.add("path/../path");
        attemptedPathManipulation.add("path/..");
        attemptedPathManipulation.add("/path");

        for (String path: attemptedPathManipulation) {
            final BookDetails bookDetails = createBookDetailsForPath("");

            final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
            assertEquals(1, violations.size(), "There should be one violation");
            violations.forEach(authorConstraintViolation ->
                assertEquals("invalid path format", authorConstraintViolation.getMessage(),
                    MessageFormat.format("{0} should be an invalid path", path)));
        }
    }

    @Test
    void testFilePathSize() {
        final String maxSize = new String(new char[256]).replace('\0', 'a');
        final BookDetails bookDetails = createBookDetailsForPath(maxSize);
        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(1, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("invalid path format", authorConstraintViolation.getMessage()));
    }

    @Test
    void testFilePathCharacters() {
        final String ascii = new String(IntStream.rangeClosed(32, 255).toArray(), 0, 95);
        final String illegal = ascii.replaceAll("[a-zA-Z0-9-._]", "");

        final char[] disallowedCharacters = illegal.toCharArray();
        final List<String> attemptedPathManipulation = new ArrayList<>();

        for (char c: disallowedCharacters) {
            attemptedPathManipulation.add(MessageFormat.format("{0}", c));
            attemptedPathManipulation.add(MessageFormat.format("{0}/", c));
            attemptedPathManipulation.add(MessageFormat.format("{0}/file", c));
            attemptedPathManipulation.add(MessageFormat.format("file{0}", c));
            attemptedPathManipulation.add(MessageFormat.format("file{0}/", c));
            attemptedPathManipulation.add(MessageFormat.format("file{0}/file", c));
            attemptedPathManipulation.add(MessageFormat.format("{0}file", c));
            attemptedPathManipulation.add(MessageFormat.format("{0}file/", c));
            attemptedPathManipulation.add(MessageFormat.format("{0}file/file", c));
            attemptedPathManipulation.add(MessageFormat.format("path/{0}", c));
            attemptedPathManipulation.add(MessageFormat.format("path/file{0}", c));
            attemptedPathManipulation.add(MessageFormat.format("path/{0}file", c));
        }

        for (String path: attemptedPathManipulation) {
            final BookDetails bookDetails = createBookDetailsForPath("");

            final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
            assertEquals(1, violations.size(), "There should be one violation");
            violations.forEach(authorConstraintViolation ->
                assertEquals("invalid path format", authorConstraintViolation.getMessage(),
                    MessageFormat.format("{0} should be an invalid path", path)));
        }
    }

    private static BookDetails createBookDetailsForPath(final String path) {
        return new BookDetails(1, "title", "author", "series",
            1, "publisher", "1234567890", "language", "format", 100, true,
            new Timestamp(0), new Timestamp(0), new Timestamp(0), path, "comments");
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}