package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.metadata.Series;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SeriesTest {
    @Test
    void testValid() {
        final Series series = new Series(1, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(series);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Series series = new Series(null, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(series);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Series series = new Series(0, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(series);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullName() {
        final Series series = new Series(1, null, "sort");
        final Set<ConstraintViolation<Object>> violations = validate(series);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}