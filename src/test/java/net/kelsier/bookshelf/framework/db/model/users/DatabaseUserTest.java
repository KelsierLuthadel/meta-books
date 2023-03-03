package net.kelsier.bookshelf.framework.db.model.users;

import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.kelsier.bookshelf.MetaBooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.validation.ConstraintViolation;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class DatabaseUserTest {
    private static final DropwizardClientExtension EXTENSION = new DropwizardClientExtension(new MetaBooks(MetaBooks.class.getClassLoader()));

    @Test
    void testValidUser() {
        final DatabaseUser user = new DatabaseUser(1, "name", "sort", "lastName",
                "email@email.com", true, "Pa££w0rds", Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    void testNullId() {
        final DatabaseUser user = new DatabaseUser(null, "name", "sort", "lastName",
                "email@email.com", true, "Pa££w0rds", Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testZeroId() {
        final DatabaseUser user = new DatabaseUser(0, "name", "sort", "lastName",
                "email@email.com", true, "Pa££w0rds", Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 1", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullName() {
        final DatabaseUser user = new DatabaseUser(1, null, "sort", "lastName",
                "email@email.com", true, "Pa££w0rds", Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be blank", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullEnabled() {
        final DatabaseUser user = new DatabaseUser(1, "name", "sort", "lastName",
                "email@email.com", null, "Pa££w0rds", Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testNullPassword() {
        final DatabaseUser user = new DatabaseUser(1, "name", "sort", "lastName",
                "email@email.com", true, null, Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);

        assertEquals(1, violations.size());
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null", authorConstraintViolation.getMessage()));
    }

    @Test
    void testPasswordCharacters() {
        final String ascii = new String(IntStream.rangeClosed(32, 126).toArray(), 0, 95);
        final String invalid = ascii.replaceAll("[ a-zA-Z0-9\\\\\\/\\-+:\\.\\[\\]|()_^,=;~<>£#$\"'`{}@!%&*?]", "");
        assertEquals(0, invalid.length());

        final String allowed = "-+:\\/[].|()_^,=;~ <>£#$\"'`{}@!%&*?";

        // Maximum length may be exceeded, so split allowed characters in half
        String firstHalf = MessageFormat.format("aA0{0}", allowed.substring(0, (allowed.length()/2)));
        String secondHalf = MessageFormat.format("aA0{0}", allowed.substring(allowed.length()/2));

        DatabaseUser user = new DatabaseUser(1, "name", "sort", "lastName",
                "email@address", true, firstHalf, Arrays.asList(1, 2, 3));
        Set<ConstraintViolation<Object>> violations = validate(user);
        assertEquals(0, violations.size());


        user = new DatabaseUser(1, "name", "sort", "lastName",
                "email@address", true, secondHalf, Arrays.asList(1, 2, 3));
        violations = validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    void testEmailBadDomains() {
        /*
        Allowed characters: DNS names can contain only alphabetic characters (A-Z), numeric characters (0-9),
        the minus sign (-), and the period (.). Period characters are allowed only when they're used to delimit the
        components of domain style names.
         */

        final String ascii = new String(IntStream.rangeClosed(32, 126).toArray(), 0, 95);
        final String illegal = ascii.replaceAll("[a-zA-Z0-9-.]", "");
        final char[] disallowedCharacters = illegal.toCharArray();

        for (char disallowedCharacter : disallowedCharacters) {
            testForBadEmail(MessageFormat.format("user@dom{0}ain", disallowedCharacter));
        }

    }

    @Test
    void testEmailBadUser() {
        /*
            The local-part of the email address may use any of these ASCII characters:

            uppercase and lowercase Latin letters A to Z and a to z;
            digits 0 to 9;
            special characters !#$%&'*+-/=?^_`{|}~;
         */

        final String ascii = new String(IntStream.rangeClosed(32, 126).toArray(), 0, 95);
        final String illegal = ascii.replaceAll("[a-zA-Z0-9-.!#$%&'*+-/=?^_`{|}~]", "");
        final char[] disallowedCharacters = illegal.toCharArray();

        for (char disallowedCharacter : disallowedCharacters) {
            testForBadEmail(MessageFormat.format("user{0}@domain", disallowedCharacter));
        }
    }

    void testForBadEmail(final String emailAddress) {
        final DatabaseUser user = new DatabaseUser(1, "name", "sort", "lastName",
                emailAddress, true, "P4££w0rd!", Arrays.asList(1, 2, 3));
        final Set<ConstraintViolation<Object>> violations = validate(user);

        assertEquals(1, violations.size());
        violations.forEach(emailConstraintViolation -> assertEquals("invalid format", emailConstraintViolation.getMessage()));
    }

    Set<ConstraintViolation<Object>> validate(final Object object) {
        return EXTENSION.getEnvironment().getValidator().validate(object);
    }

}