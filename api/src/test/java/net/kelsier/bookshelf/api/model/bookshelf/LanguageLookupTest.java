package net.kelsier.bookshelf.api.model.bookshelf;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.BookLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.LanguageLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LanguageLookupTest {
    private static final String FIELD = "lang_code";

    @Test
    void testValidField() {
        assertEquals(0, validate(new LanguageLookup("lang_code", Operator.EQ, "one")).size(),
                "The lang_code field should be valid");
    }

    @Test
    void testInvalidField() {
        final LanguageLookup lookup = new LanguageLookup("invalid", Operator.EQ, "one");
        final Set<ConstraintViolation<Object>> violations = validate(lookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [lang_code]", violation.getMessage());
    }

    @Test
    void testValidOperators() {
        assertEquals(0, validate(new LanguageLookup(FIELD, Operator.EQ, "one")).size(),
                "The constraint EQ should be valid");

        assertEquals(0, validate(new LanguageLookup(FIELD, Operator.NEQ, "one")).size(),
                "The constraint NEQ should be valid");

        assertEquals(0, validate(new LanguageLookup(FIELD, Operator.LIKE, "one")).size(),
                "The constraint LIKE should be valid");

        assertEquals(0, validate(new LanguageLookup(FIELD, Operator.UNLIKE, "one")).size(),
                "The constraint UNLIKE should be valid");
    }

    @Test
    void testInvalidOperator() {
        final LanguageLookup lookup = new LanguageLookup(FIELD, Operator.GT, "one");
        final Set<ConstraintViolation<Object>> violations = validate(lookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [EQ, NEQ, LIKE, UNLIKE]", violation.getMessage());
    }

    @Test
    void testInvalidOperators() {
        assertEquals(1, validate(new LanguageLookup(FIELD, Operator.GT, "one")).size(),
                "The constraint GT should be invalid");

        assertEquals(1, validate(new LanguageLookup(FIELD, Operator.LT, "one")).size(),
                "The constraint LT should be invalid");

        assertEquals(1, validate(new LanguageLookup(FIELD, Operator.GTE, "one")).size(),
                "The constraint GTE should be invalid");

        assertEquals(1, validate(new LanguageLookup(FIELD, Operator.LTE, "one")).size(),
                "The constraint LTE should be invalid");
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