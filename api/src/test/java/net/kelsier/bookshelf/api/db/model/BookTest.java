package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class BookTest {
    @Test
    void testValidBook() {
        final Book book = new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                1, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(0, violations.size());
    }

    @Test
    void testDatesGMT() throws ParseException {
        final Timestamp date = new Timestamp(getTime("2023-03-26 23:59:59"));

        final Book book = new Book(1, "title", "sort", date, date,
            2, "","path", true,date);

        assertEquals(date,book.getDateAdded());
        assertEquals(date,book.getPublicationDate());
        assertEquals(date,book.getLastModified());
    }

    @Test
    void testDatesBST() throws ParseException {
        final Timestamp date = new Timestamp(getTime("2023-10-28 23:59:59"));

        final Book book = new Book(1, "title", "sort", date, date,
            3, "","path", true,date);

        assertEquals(date,book.getDateAdded());
        assertEquals(date,book.getPublicationDate());
        assertEquals(date,book.getLastModified());
    }

    private static long getTime(final String dateTime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(dateTime);
        return date.getTime();
    }

    @Test
    void testNullId() {
        final Book book = new Book(null, "title", "sort", new Timestamp(0), new Timestamp(0),
                4, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Book book = new Book(0, "title", "sort", new Timestamp(0), new Timestamp(0),
                5, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullTitle() {
        final Book book = new Book(1, null, "sort", new Timestamp(0), new Timestamp(0),
                6, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullSeriesIndex() {
        final Book book = new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                null, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPath() {
        final Book book = new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                7, "",null, true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullModified() {
        final Book book = new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                8, "","path", true, null);

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}