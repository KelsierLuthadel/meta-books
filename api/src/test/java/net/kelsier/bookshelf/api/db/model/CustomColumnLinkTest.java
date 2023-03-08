package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CustomColumnLinkTest {
    @Test
    void testValid() {
        final CustomColumnLink link = new CustomColumnLink(1, 1, 1);

        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final CustomColumnLink link = new CustomColumnLink(null, 1, 1);

        zeroIdTest(link, "must not be null");
    }

    @Test
    void testZeroId() {
        final CustomColumnLink link = new CustomColumnLink(0, 1, 1);

        zeroIdTest(link, "must be greater than or equal to 1");
    }

    private void zeroIdTest(final CustomColumnLink link, final String expected) {
        final Set<ConstraintViolation<Object>> violations = validate(link);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals(expected, authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final CustomColumnLink link = new CustomColumnLink(1, null, 1);

        zeroIdTest(link, "must not be null");
    }

    @Test
    void testZeroBook() {
        final CustomColumnLink link = new CustomColumnLink(1, 0, 1);

        zeroIdTest(link, "must be greater than or equal to 1");
    }

    @Test
    void testNullValue() {
        final CustomColumnLink link = new CustomColumnLink(1, 1, null);

        zeroIdTest(link, "must not be null");
    }

    @Test
    void testZeroLink() {
        final CustomColumnLink link = new CustomColumnLink(1, 1, 0);

        zeroIdTest(link, "must be greater than or equal to 1");
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}