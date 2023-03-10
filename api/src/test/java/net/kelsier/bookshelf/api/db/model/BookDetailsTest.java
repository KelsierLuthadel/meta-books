package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookDetailsTest {
    public static final int ID = 1;
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String SERIES = "series";
    public static final String PUBLISHER = "publisher";
    public static final String ISBN = "1234567890";
    public static final String LANGUAGE = "language";
    public static final String FORMAT = "format";
    public static final int SIZE = 100;
    public static final boolean HAS_COVER = true;
    public static final Timestamp DATE = new Timestamp(0);
    public static final String PATH = "path";
    public static final String COMMENTS = "comments";

    public static final String IDENTIFIER_TYPES = "a,b,c";

    public static final String IDENTIFIER_VALUES = "1,2,3";

    @Test
    void testValidAuthor() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(0, violations.size(), "There should be no violations");
    }

    @Test
    void testNullId() {
        final BookDetails bookDetails = new BookDetails(null, TITLE, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinimumFailId() {
        final BookDetails bookDetails = new BookDetails(0, TITLE, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals(
            "must be greater than or equal to 1",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullTitle() {
        final BookDetails bookDetails = new BookDetails(ID, null, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullAuthor() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, null, SERIES,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeries() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, null,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeriesIndex() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
                null, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPublisher() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, null, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinIsbn() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", "123",IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("length must be between 10 and 17", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMaxIsbn() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", "123456789012345678", IDENTIFIER_TYPES, IDENTIFIER_VALUES,LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("length must be between 10 and 17", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLanguage() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, null, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullFormat() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, null, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSize() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, null, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinSize() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, -ID, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 0", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLastModified() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, null, PATH, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPath() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, null, COMMENTS);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullComments() {
        final BookDetails bookDetails = new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, "publisher", ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES, LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testEmptyFilePath() {
        final BookDetails bookDetails = createBookDetailsForPath("");
        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
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
            assertEquals(ID, violations.size(), "There should be one violation");
            violations.forEach(authorConstraintViolation ->
                assertEquals("invalid path format", authorConstraintViolation.getMessage(),
                    MessageFormat.format("{0} should be an invalid path", path)));
        }
    }

    @Test
    void testFilePathSize() {
        final String maxSize = new String(new char[257]).replace('\0', 'a');
        final BookDetails bookDetails = createBookDetailsForPath(maxSize);
        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("invalid path format", authorConstraintViolation.getMessage()));
    }

    @Test
    void testBookPathWithParentesis() {
        final BookDetails bookDetails = createBookDetailsForPath("Author name/The book title (1234)");
        final Set<ConstraintViolation<Object>> violations = validate(bookDetails);
        assertEquals(0, violations.size(), "There should be no violations");
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
            assertEquals(ID, violations.size(), "There should be one violation");
            violations.forEach(authorConstraintViolation ->
                assertEquals("invalid path format", authorConstraintViolation.getMessage(),
                    MessageFormat.format("{0} should be an invalid path", path)));
        }
    }

    private static BookDetails createBookDetailsForPath(final String path) {
        return new BookDetails(ID, TITLE, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, IDENTIFIER_TYPES, IDENTIFIER_VALUES,LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, path, COMMENTS);
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}