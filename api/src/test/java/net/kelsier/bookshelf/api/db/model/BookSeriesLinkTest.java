package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.links.BookSeriesLink;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookSeriesLinkTest {
    @Test
    void testValid() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(bookSeriesLink);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(null, 1, 1);

        zeroIdTest(bookSeriesLink, "must not be null");
    }

    @Test
    void testZeroId() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(0, 1, 1);

        zeroIdTest(bookSeriesLink, "must be greater than or equal to 1");
    }

    private void zeroIdTest(final BookSeriesLink bookSeriesLink, final String expected) {
        final Set<ConstraintViolation<Object>> violations = validate(bookSeriesLink);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals(expected, authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(1, null, 1);

        zeroIdTest(bookSeriesLink, "must not be null");
    }

    @Test
    void testZeroBook() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(1, 0, 1);

        zeroIdTest(bookSeriesLink, "must be greater than or equal to 1");
    }

    @Test
    void testNullSeries() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(1, 1, null);

        zeroIdTest(bookSeriesLink, "must not be null");
    }

    @Test
    void testZeroSeries() {
        final BookSeriesLink bookSeriesLink = new BookSeriesLink(1, 1, 0);

        zeroIdTest(bookSeriesLink, "must be greater than or equal to 1");
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}