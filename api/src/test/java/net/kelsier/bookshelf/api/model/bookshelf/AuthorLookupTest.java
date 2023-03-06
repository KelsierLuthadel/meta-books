package net.kelsier.bookshelf.api.model.bookshelf;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.AuthorLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class AuthorLookupTest {
    private static final String FIELD = "name";

    @Test
    void testValidField() {
        assertEquals(0, validate(new AuthorLookup(FIELD, Operator.EQ, "one")).size(),
                "The name field should be valid");
    }

    @Test
    void testInvalidField() {
        final AuthorLookup authorLookup = new AuthorLookup("invalid", Operator.EQ, "one");
        final Set<ConstraintViolation<Object>> violations = validate(authorLookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [name]", violation.getMessage());
    }

    @Test
    void testValidOperators() {
        assertEquals(0, validate(new AuthorLookup(FIELD, Operator.EQ, "one")).size(),
                "The constraint EQ should be valid");

        assertEquals(0, validate(new AuthorLookup(FIELD, Operator.NEQ, "one")).size(),
                "The constraint NEQ should be valid");

        assertEquals(0, validate(new AuthorLookup(FIELD, Operator.LIKE, "one")).size(),
                "The constraint LIKE should be valid");

        assertEquals(0, validate(new AuthorLookup(FIELD, Operator.UNLIKE, "one")).size(),
                "The constraint UNLIKE should be valid");
    }

    @Test
    void testInvalidOperator() {
        final AuthorLookup authorLookup = new AuthorLookup(FIELD, Operator.GT, "one");
        final Set<ConstraintViolation<Object>> violations = validate(authorLookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [EQ, NEQ, LIKE, UNLIKE]", violation.getMessage());
    }

    @Test
    void testInvalidOperators() {
        assertEquals(1, validate(new AuthorLookup(FIELD, Operator.GT, "one")).size(),
                "The constraint GT should be invalid");

        assertEquals(1, validate(new AuthorLookup(FIELD, Operator.LT, "one")).size(),
                "The constraint LT should be invalid");

        assertEquals(1, validate(new AuthorLookup(FIELD, Operator.GTE, "one")).size(),
                "The constraint GTE should be invalid");

        assertEquals(1, validate(new AuthorLookup(FIELD, Operator.LTE, "one")).size(),
                "The constraint LTE should be invalid");
    }

    @Test
    void testLookupPlain() {
        final AuthorLookup lookup = new AuthorLookup(FIELD, Operator.EQ, "one");
        assertEquals("one", lookup.getValue());
        assertEquals("one", lookup.getLookupValue());
    }

    @Test
    void testLookupPlainLike() {
        final AuthorLookup lookup = new AuthorLookup(FIELD, Operator.LIKE, "one");
        assertEquals("one", lookup.getValue());
        assertEquals("%one%", lookup.getLookupValue());
    }

    @Test
    void testLookupPlainUnlike() {
        final AuthorLookup lookup = new AuthorLookup(FIELD, Operator.UNLIKE, "one");
        assertEquals("one", lookup.getValue());
        assertEquals("%one%", lookup.getLookupValue());
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}