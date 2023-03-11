package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.metadata.Tag;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TagTest {
    @Test
    void testValid() {
        final Tag tag = new Tag(1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(tag);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Tag tag = new Tag(null, "name");
        final Set<ConstraintViolation<Object>> violations = validate(tag);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Tag tag = new Tag(0, "name");
        final Set<ConstraintViolation<Object>> violations = validate(tag);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullName() {
        final Tag tag = new Tag(1, null);
        final Set<ConstraintViolation<Object>> violations = validate(tag);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }


    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}