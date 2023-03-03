package net.kelsier.bookshelf.framework.db.model.bookshelf;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class CommentTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValid() {
        final Comment comment = new Comment(1, 1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Comment comment = new Comment(null, 1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Comment comment = new Comment(0, 1, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullBook() {
        final Comment comment = new Comment(1, null, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroBook() {
        final Comment comment = new Comment(1, 0, "text");

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullComment() {
        final Comment comment = new Comment(1, 1, null);

        final Set<ConstraintViolation<Object>> violations = validate(comment);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}