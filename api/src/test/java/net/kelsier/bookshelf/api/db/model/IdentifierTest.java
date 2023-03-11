package net.kelsier.bookshelf.api.db.model;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.db.model.metadata.Identifier;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class IdentifierTest {
    @Test
    void testValid() {
        final Identifier identifier = new Identifier(1, 1, "text", "value");
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Identifier identifier = new Identifier(null, 1, "text", "value");
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Identifier identifier = new Identifier(0, 1, "text", "value");
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final Identifier identifier = new Identifier(1, null, "text", "value");
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final Identifier identifier = new Identifier(1, 0, "text", "value");
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullType() {
        final Identifier identifier = new Identifier(1, 1, null, "value");
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullValue() {
        final Identifier identifier = new Identifier(1, 1, "text", null);
        final Set<ConstraintViolation<Object>> violations = validate(identifier);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}