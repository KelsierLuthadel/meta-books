package net.kelsier.bookshelf.api.model.common;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class PaginationTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValidPagination() {
        assertEquals(0, validate(new Pagination(0, 10, new Sort("name", "asc"))).size(),
                "Pagination should be valid");
    }

    @Test
    void testValidPaginationDefaults() {
        final Pagination pagination = new Pagination(0, 10, null);
        final Set<ConstraintViolation<Object>> violations = validate(pagination);
        assertEquals(0, violations.size(), "There should be no violations");
        assertEquals(0, pagination.getStart(), "Start should be 0");
        assertEquals(10, pagination.getLimit(), "Limit should be 10");
        assertEquals("id", pagination.getSort().getField(), "Default field should be applied");
        assertEquals("asc", pagination.getSort().getDirection(), "Default field should be applied");

    }

    @Test
    void testInvalidStart() {
        final Pagination pagination = new Pagination(null, 10, new Sort("name", "asc"));

        final Set<ConstraintViolation<Object>> violations = validate(pagination);
        assertEquals(1, violations.size(), "There should be one violation");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testInvalidStartMin() {
        final Pagination pagination = new Pagination(-1, 10, new Sort("name", "asc"));

        final Set<ConstraintViolation<Object>> violations = validate(pagination);
        assertEquals(1, violations.size(), "There should be one violation");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be greater than or equal to 0", violation.getMessage());
    }

    @Test
    void testInvalidLimit() {
        final Pagination pagination = new Pagination(0, null, new Sort("name", "asc"));

        final Set<ConstraintViolation<Object>> violations = validate(pagination);
        assertEquals(1, violations.size(), "There should be one violation");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
    }

    @Test
    void testInvalidLimitMin() {
        final Pagination pagination = new Pagination(0, -1, new Sort("name", "asc"));

        final Set<ConstraintViolation<Object>> violations = validate(pagination);
        assertEquals(1, violations.size(), "There should be one violation");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be greater than or equal to 0", violation.getMessage());
    }

    @Test
    void testInvalidLimitMax() {
        final Pagination pagination = new Pagination(0, 101, new Sort("name", "asc"));

        final Set<ConstraintViolation<Object>> violations = validate(pagination);
        assertEquals(1, violations.size(), "There should be one violation");
        final ConstraintViolation<Object> violation = violations.iterator().next();
        assertEquals("must be less than or equal to 100", violation.getMessage());
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }
}