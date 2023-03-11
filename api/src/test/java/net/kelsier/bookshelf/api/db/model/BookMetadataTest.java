package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.view.BookMetadata;
import net.kelsier.bookshelf.api.db.model.view.Identifier;
import net.kelsier.bookshelf.api.db.model.view.Tags;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookMetadataTest {
    private static final int ID = 1;
    private static final String TITLE = "title";
    private static final Integer AUTHOR_ID = 1;
    private static final String AUTHOR = "author";
    private static final String SERIES = "series";
    private static final String PUBLISHER = "publisher";
    private static final String ISBN = "1234567890";
    private static final String LANGUAGE = "language";
    private static final String FORMAT = "format";
    private static final int SIZE = 100;
    private static final boolean HAS_COVER = true;
    private static final Timestamp DATE = new Timestamp(0);
    private static final String PATH = "path";
    private static final String COMMENTS = "comments";
    public static final String[] IDENTIFIER_TYPES = new String [] {"provider-a", "provider-b", "provider-c"};
    public static final String[] IDENTIFIER_VALUES = {"book-reference-1", "book-reference-2", "book-reference-3"};
    public static final String[] TAGS = {"tag-1", "tag-2", "tag-3"};

    @Test
    void testValidAuthor() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE,
                HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(0, violations.size(), "There should be no violations");
    }

    @Test
    void testNullId() {
        final BookMetadata bookMetadata = new BookMetadata(null, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE,
                HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinimumFailId() {
        final BookMetadata bookMetadata = new BookMetadata(0, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE,
                HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals(
            "must be greater than or equal to 1",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullTitle() {
        final BookMetadata bookMetadata = new BookMetadata(ID, null, AUTHOR_ID, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullAuthor() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, null, SERIES,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE, HAS_COVER,
                DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeries() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, null,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE,
                HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeriesIndex() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
                null, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT,
                SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPublisher() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, null, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE,
                HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLanguage() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), null,
                FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullFormat() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, null,
                SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSize() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, null, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinSize() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, -ID, HAS_COVER,
            DATE, DATE, DATE, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 0", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLastModified() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, null, PATH, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPath() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, null, COMMENTS, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullComments() {
        final BookMetadata bookMetadata = new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, "publisher", ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, PATH, null, new Tags(TAGS));

        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testEmptyFilePath() {
        final BookMetadata bookMetadata = createBookDetailsForPath("");
        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
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
            final BookMetadata bookMetadata = createBookDetailsForPath("");

            final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
            assertEquals(ID, violations.size(), "There should be one violation");
            violations.forEach(authorConstraintViolation ->
                assertEquals("invalid path format", authorConstraintViolation.getMessage(),
                    MessageFormat.format("{0} should be an invalid path", path)));
        }
    }

    @Test
    void testFilePathSize() {
        final String maxSize = new String(new char[257]).replace('\0', 'a');
        final BookMetadata bookMetadata = createBookDetailsForPath(maxSize);
        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
        assertEquals(ID, violations.size(), "There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("invalid path format", authorConstraintViolation.getMessage()));
    }

    @Test
    void testBookPathWithParentesis() {
        final BookMetadata bookMetadata = createBookDetailsForPath("Author name/The book title (1234)");
        final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
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
            final BookMetadata bookMetadata = createBookDetailsForPath("");

            final Set<ConstraintViolation<Object>> violations = validate(bookMetadata);
            assertEquals(ID, violations.size(), "There should be one violation");
            violations.forEach(authorConstraintViolation ->
                assertEquals("invalid path format", authorConstraintViolation.getMessage(),
                    MessageFormat.format("{0} should be an invalid path", path)));
        }
    }

    private static BookMetadata createBookDetailsForPath(final String path) {
        return new BookMetadata(ID, TITLE, AUTHOR_ID, AUTHOR, SERIES,
            ID, PUBLISHER, ISBN, new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES),LANGUAGE, FORMAT, SIZE, HAS_COVER,
            DATE, DATE, DATE, path, COMMENTS, new Tags(TAGS));
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}