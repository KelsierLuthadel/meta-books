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
class RatingTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValid() {
        final Rating rating = new Rating(1, 1);
        final Set<ConstraintViolation<Object>> violations = validate(rating);

        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final Rating rating = new Rating(null, 1);
        final Set<ConstraintViolation<Object>> violations = validate(rating);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final Rating rating = new Rating(0, 1);
        final Set<ConstraintViolation<Object>> violations = validate(rating);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }


    @Test
    void testMinRating() {
        final Rating rating = new Rating(1, -1);
        final Set<ConstraintViolation<Object>> violations = validate(rating);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 0", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMaxRating() {
        final Rating rating = new Rating(1, 11);
        final Set<ConstraintViolation<Object>> violations = validate(rating);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be less than or equal to 10", authorConstraintViolation.getMessage()));
    }
    

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}