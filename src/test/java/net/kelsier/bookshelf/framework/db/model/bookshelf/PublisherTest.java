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
class PublisherTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValid() {
        final Publisher publisher = new Publisher(1, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(publisher);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Publisher publisher = new Publisher(null, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(publisher);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testZeroId() {
        final Publisher publisher = new Publisher(0, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(publisher);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage());
        });
    }

    @Test
    void testNullName() {
        final Publisher publisher = new Publisher(1, null, "sort");
        final Set<ConstraintViolation<Object>> violations = validate(publisher);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> {
            assertEquals("must not be null", authorConstraintViolation.getMessage());
        });
    }


    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}