package net.kelsier.bookshelf.api.model.bookshelf;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.BookLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.CommentLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class CommentLookupTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));
    private static final String FIELD = "text";

    @Test
    void testValidField() {
        assertEquals(0, validate(new CommentLookup("text", Operator.EQ, "one")).size(),
                "The text field should be valid");
    }

    @Test
    void testInvalidField() {
        final CommentLookup lookup = new CommentLookup("invalid", Operator.EQ, "one");
        final Set<ConstraintViolation<Object>> violations = validate(lookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [text]", violation.getMessage());
    }

    @Test
    void testValidOperators() {
        assertEquals(0, validate(new CommentLookup(FIELD, Operator.EQ, "one")).size(),
                "The constraint EQ should be valid");

        assertEquals(0, validate(new CommentLookup(FIELD, Operator.NEQ, "one")).size(),
                "The constraint NEQ should be valid");

        assertEquals(0, validate(new CommentLookup(FIELD, Operator.LIKE, "one")).size(),
                "The constraint LIKE should be valid");

        assertEquals(0, validate(new CommentLookup(FIELD, Operator.UNLIKE, "one")).size(),
                "The constraint UNLIKE should be valid");
    }

    @Test
    void testInvalidOperator() {
        final CommentLookup lookup = new CommentLookup(FIELD, Operator.GT, "one");
        final Set<ConstraintViolation<Object>> violations = validate(lookup);
        assertEquals(1, violations.size(), "The field name should be invalid");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be one of [EQ, NEQ, LIKE, UNLIKE]", violation.getMessage());
    }

    @Test
    void testInvalidOperators() {
        assertEquals(1, validate(new CommentLookup(FIELD, Operator.GT, "one")).size(),
                "The constraint GT should be invalid");

        assertEquals(1, validate(new CommentLookup(FIELD, Operator.LT, "one")).size(),
                "The constraint LT should be invalid");

        assertEquals(1, validate(new CommentLookup(FIELD, Operator.GTE, "one")).size(),
                "The constraint GTE should be invalid");

        assertEquals(1, validate(new CommentLookup(FIELD, Operator.LTE, "one")).size(),
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

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}