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
class BookRatingLinkTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValid() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookRatingLink bookRatingLink = new BookRatingLink(null, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final BookRatingLink bookRatingLink = new BookRatingLink(0, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, null, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 0, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullRating() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroRating() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 1, 0);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }


    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}