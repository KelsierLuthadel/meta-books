package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CustomColumnsTest {
    @Test
    void testValid() {
        final CustomColumns customColumns = new CustomColumns(1, "text", "name", "dataType",
                "display", true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final CustomColumns customColumns = new CustomColumns(null, "text", "name", "dataType",
                "display", true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final CustomColumns customColumns = new CustomColumns(0, "text", "name", "dataType",
                "display", true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullLabel() {
        final CustomColumns customColumns = new CustomColumns(1, null, "name", "dataType",
                "display", true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullName() {
        final CustomColumns customColumns = new CustomColumns(1, "text", null, "dataType",
                "display", true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullDataType() {
        final CustomColumns customColumns = new CustomColumns(1, "text", "name", null,
                "display", true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullDisplay() {
        final CustomColumns customColumns = new CustomColumns(1, "text", "name", "dataType",
                null, true, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullMultiple() {
        final CustomColumns customColumns = new CustomColumns(1, "text", "name", "dataType",
                "display", null, true);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullNormalized() {
        final CustomColumns customColumns = new CustomColumns(1, "text", "name", "dataType",
                "display", true, null);

        final Set<ConstraintViolation<Object>> violations = validate(customColumns);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }


private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}