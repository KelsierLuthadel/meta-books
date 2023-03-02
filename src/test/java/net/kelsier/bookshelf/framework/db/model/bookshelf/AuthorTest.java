package net.kelsier.bookshelf.framework.db.model.bookshelf;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class AuthorTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValidAuthor() {
        final Author author = new Author(1, "name", "sort");
        final Set<ConstraintViolation<Author>> violations = validate(author);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Author author = new Author(null, "name", "sort");
        final Set<ConstraintViolation<Author>> violations = validate(author);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testMinimumFailId() {
        final Author author = new Author(0, "name", "sort");
        final Set<ConstraintViolation<Author>> violations = validate(author);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullName() {
        final Author author = new Author(1, null, "sort");
        final Set<ConstraintViolation<Author>> violations = validate(author);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    Set<ConstraintViolation<Author>> validate(final Author object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}