package net.kelsier.bookshelf.api.model.bookshelf;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.BookLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.RatingLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RatingLookupTest {
    private static final String FIELD = "rating";

    @Test
    void testValidField() {
        assertEquals(0, validate(new RatingLookup("rating", Operator.EQ, "one")).size(),
                "The text field should be valid");
    }

    @Test
    void testInvalidField() {
        final RatingLookup lookup = new RatingLookup("invalid", Operator.EQ, "one");
        final Set<ConstraintViolation<Object>> violations = validate(lookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [rating]", violation.getMessage());
    }

    @Test
    void testValidOperators() {
        assertEquals(0, validate(new RatingLookup(FIELD, Operator.EQ, "one")).size(),
                "The constraint EQ should be valid");

        assertEquals(0, validate(new RatingLookup(FIELD, Operator.NEQ, "one")).size(),
                "The constraint NEQ should be valid");

        assertEquals(0, validate(new RatingLookup(FIELD, Operator.GT, "one")).size(),
                "The constraint GT should be valid");

        assertEquals(0, validate(new RatingLookup(FIELD, Operator.LT, "one")).size(),
                "The constraint LT should be valid");

        assertEquals(0, validate(new RatingLookup(FIELD, Operator.GTE, "one")).size(),
                "The constraint GTE should be valid");

        assertEquals(0, validate(new RatingLookup(FIELD, Operator.LTE, "one")).size(),
                "The constraint LTE should be valid");
    }

    @Test
    void testInvalidOperators() {
        assertEquals(1, validate(new RatingLookup(FIELD, Operator.LIKE, "one")).size(),
                "The constraint LIKE should be invalid");

        assertEquals(1, validate(new RatingLookup(FIELD, Operator.UNLIKE, "one")).size(),
                "The constraint UNLIKE should be invalid");
    }

    @Test
    void testInvalidOperator() {
        final RatingLookup lookup = new RatingLookup(FIELD, Operator.LIKE, "one");
        final Set<ConstraintViolation<Object>> violations = validate(lookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [EQ, NEQ, GT, LT, GTE, LTE]", violation.getMessage());
    }

    @Test
    void testLookupPlain() {
        final BookLookup lookup = new BookLookup(FIELD, Operator.EQ, "one");
        assertEquals("one", lookup.getValue());
        assertEquals("one", lookup.getLookupValue());
    }

    @Test
    void testLookupPlainLike() {
        final BookLookup lookup = new BookLookup(FIELD, Operator.LIKE, "one");
        assertEquals("one", lookup.getValue());
        assertEquals("%one%", lookup.getLookupValue());
    }

    @Test
    void testLookupPlainUnlike() {
        final BookLookup lookup = new BookLookup(FIELD, Operator.UNLIKE, "one");
        assertEquals("one", lookup.getValue());
        assertEquals("%one%", lookup.getLookupValue());
    }

   private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }

}