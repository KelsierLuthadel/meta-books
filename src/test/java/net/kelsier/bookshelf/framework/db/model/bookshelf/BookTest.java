package net.kelsier.bookshelf.framework.db.model.bookshelf;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class BookTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValidBook() {
        final Book book =  new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                1.0, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Book book =  new Book(null, "title", "sort", new Timestamp(0), new Timestamp(0),
                1.0, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testZeroId() {
        final Book book =  new Book(0, "title", "sort", new Timestamp(0), new Timestamp(0),
                1.0, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullTitle() {
        final Book book =  new Book(1, null, "sort", new Timestamp(0), new Timestamp(0),
                1.0, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullSeriesIndex() {
        final Book book =  new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                null, "","path", true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullPath() {
        final Book book =  new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                1.0, "",null, true, new Timestamp(0));

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullModified() {
        final Book book =  new Book(1, "title", "sort", new Timestamp(0), new Timestamp(0),
                1.0, "","path", true, null);

        final Set<ConstraintViolation<Object>> violations = validate(book);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}