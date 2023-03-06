package net.kelsier.bookshelf.framework.db.model.users;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.framework.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(DropwizardExtensionsSupport.class)
class DatabaseUserRoleTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValidUserRole() {
        final DatabaseUserRole userRole = new DatabaseUserRole(1, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(userRole);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final DatabaseUserRole userRole = new DatabaseUserRole(null, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(userRole);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final DatabaseUserRole userRole = new DatabaseUserRole(0, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(userRole);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testMinimumFailId() {
        final DatabaseUserRole userRole = new DatabaseUserRole(0, "name", "sort");
        final Set<ConstraintViolation<Object>> violations = validate(userRole);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullRole() {
        final DatabaseUserRole userRole = new DatabaseUserRole(1, null, "sort");
        final Set<ConstraintViolation<Object>> violations = validate(userRole);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullDescription() {
        final DatabaseUserRole userRole = new DatabaseUserRole(1, "role", null);
        final Set<ConstraintViolation<Object>> violations = validate(userRole);
        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}