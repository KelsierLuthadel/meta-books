package net.kelsier.bookshelf.framework.db.model.bookshelf;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class BookAuthorLinkTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValid() {
        final BookAuthorLink bookAuthorLink =  new BookAuthorLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookAuthorLink bookAuthorLink =  new BookAuthorLink(null, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookAuthorLink bookAuthorLink =  new BookAuthorLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullAuthor() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroAuthor() {
        final BookAuthorLink bookAuthorLink = new BookAuthorLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(bookAuthorLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}