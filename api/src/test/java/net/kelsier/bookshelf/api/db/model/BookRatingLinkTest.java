package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookRatingLinkTest {

    @Test
    void testValid() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookRatingLink bookRatingLink = new BookRatingLink(null, 1, 1);

        zeroIdTest(bookRatingLink, "must not be null");
    }

    @Test
    void testZeroId() {
        final BookRatingLink bookRatingLink = new BookRatingLink(0, 1, 1);

        zeroIdTest(bookRatingLink, "must be greater than or equal to 1");
    }

    private void zeroIdTest(final BookRatingLink bookRatingLink, final String expected) {
        final Set<ConstraintViolation<Object>> violations = validate(bookRatingLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals(expected, authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, null, 1);

        zeroIdTest(bookRatingLink, "must not be null");
    }

    @Test
    void testZeroBook() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 0, 1);

        zeroIdTest(bookRatingLink, "must be greater than or equal to 1");
    }

    @Test
    void testNullRating() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 1, null);

        zeroIdTest(bookRatingLink, "must not be null");
    }

    @Test
    void testZeroRating() {
        final BookRatingLink bookRatingLink = new BookRatingLink(1, 1, 0);

        zeroIdTest(bookRatingLink, "must be greater than or equal to 1");
    }


    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}