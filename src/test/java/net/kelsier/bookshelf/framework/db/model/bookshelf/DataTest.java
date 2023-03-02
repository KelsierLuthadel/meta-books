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
class DataTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValid() {
        final Data data = new Data(1, 1, "text", 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(data);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Data data = new Data(null, 1, "text", 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(data);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testZeroId() {
        final Data data = new Data(0, 1, "text", 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(data);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullFormat() {
        final Data data = new Data(1, 1, null, 1, "name");
        final Set<ConstraintViolation<Object>> violations = validate(data);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullSize() {
        final Data data = new Data(1, 1, "text", null, "name");
        final Set<ConstraintViolation<Object>> violations = validate(data);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullName() {
        final Data data = new Data(1, 1, "text", 1, null);
        final Set<ConstraintViolation<Object>> violations = validate(data);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}