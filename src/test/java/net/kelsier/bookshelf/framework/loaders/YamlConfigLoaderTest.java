package net.kelsier.bookshelf.framework.loaders;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;
import net.kelsier.bookshelf.framework.encryption.Cipher;
import net.kelsier.bookshelf.framework.encryption.Encrypted;
import net.kelsier.bookshelf.framework.encryption.exception.CipherException;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
public class YamlConfigLoaderTest {
    @Test
    public void testValidReadConfiguration() throws ConfigurationException {
        final ValidRead configuration = loadTestConfiguration(ValidRead.class);
        assertEquals(5, configuration.testInteger);
        assertEquals("STR", configuration.testString);
    }

    @Test
    public void testInvalidReadConfiguration() {
        assertThrows(ConfigurationException.class, () -> loadTestConfiguration(InvalidRead.class));
    }

    @Test
    public void testCasting() throws  ConfigurationException {
        final Casting configuration = loadTestConfiguration(Casting.class);
        assertEquals(5, configuration.testInteger);
        assertEquals("5", configuration.testString);
    }




    @Test
    public void testReadConfigurationNested() throws  ConfigurationException {
        final Outer configuration = loadTestConfiguration(Outer.class);
        assertEquals(5, configuration.testInteger);
        assertEquals("STR", configuration.testString);
        assertEquals("STR", configuration.testInner.testString);
    }


    @SuppressWarnings("rawtypes")
    private void checkViolationExists(final Set<ConstraintViolation> violations, final String configField, final String vMessage) {
        assertTrue(violations.stream().anyMatch(
                        conVi -> conVi.getPropertyPath().toString().equals(configField) && conVi.getMessage().equals(vMessage)),
                "Expected violation:\n" + configField + " : " + vMessage + "\nActual violations:\n" + violations
        );
    }

    @SuppressWarnings("unused")
    private static final class ValidRead {
        @NotNull
        @Min(5)
        @Max(6)
        private int testInteger;

        @Encrypted
        @NotNull
        @Size(min = 1, max = 5)
        private String testString;

        public int getTestInteger() {
            return testInteger;
        }

        public String getTestString() {
            return testString;
        }
    }

    private <T> T loadTestConfiguration(final Class<T> configurationClass) throws ConfigurationException {
        String configPath = "src/test/resources/config/";
        final YamlConfigLoader configLoader = new YamlConfigLoader(configPath, new TestCipher());
        return configLoader.loadConfiguration(configurationClass);
    }


    @SuppressWarnings("unused")
    private static final class InvalidRead {
        private int testInteger;
        private String testString;

        public int getTestInteger() {
            return testInteger;
        }

        public String getTestString() {
            return testString;
        }
    }


    @SuppressWarnings("unused")
    private static final class Casting {
        private int testInteger;
        private String testString;

        public int getTestInteger() {
            return testInteger;
        }

        public String getTestString() {
            return testString;
        }
    }

    @SuppressWarnings("unused")
    private static final class ExceptionRead {
        @Encrypted
        @NotNull
        private String testString;

        public String getTestString() {
            return testString;
        }
    }


    @SuppressWarnings("unused")
    private static final class Violation {
        @NotNull
        @Min(5)
        @Max(6)
        private int testInteger;

        @NotNull
        @Size(min = 1, max = 5)
        private String testString;

        public int getTestInteger() {
            return testInteger;
        }

        public String getTestString() {
            return testString;
        }
    }


    @SuppressWarnings("unused")
    private static final class Outer {
        @NotNull
        @Min(5)
        @Max(6)
        private int testInteger;

        @Encrypted
        @NotNull
        @Size(min = 1, max = 5)
        private String testString;

        @NotNull
        @Valid
        private Inner testInner;

        public String getTestString() {
            return testString;
        }

        public int getTestInteger() {
            return testInteger;
        }

        public Inner getTestInner() {
            return testInner;
        }
    }

    @SuppressWarnings("unused")
    private static final class Inner {
        @Encrypted
        @NotNull
        @Size(min = 1, max = 5)
        private String testString;

        public String getTestString() {
            return testString;
        }
    }

    @SuppressWarnings("unused")
    private static final class ErrorEncryptOuter {
        @NotNull
        @Valid
        private Inner testInner;

        public Inner getTestInner() {
            return testInner;
        }
    }


    @SuppressWarnings("unused")
    private static final class DecryptStringWithAnnotation {
        @Encrypted
        @NotNull
        @Valid
        @JsonSetter(value = "string", nulls = Nulls.SKIP)
        private String string;

        public String getString() {
            return string;
        }

        @JsonSetter(value = "string", nulls = Nulls.SKIP)
        public void setString(final String string) {
            this.string = string;
        }
    }

    @SuppressWarnings("unused")
    private static final class InvalidEncryptOuter {
        @NotNull
        @Valid
        private EncryptedIntegerInner testInner;

        public EncryptedIntegerInner getTestInner() {
            return testInner;
        }
    }


    @SuppressWarnings("unused")
    private static final class EncryptedIntegerInner {
        @Encrypted
        @NotNull
        @Size(min = 1, max = 5)
        private int testInt;

        public int getTestInt() {
            return testInt;
        }
    }

    private static final class TestCipher implements Cipher {
        @Override
        public String decrypt(final String input) throws CipherException {
            if (input.equals("ERROR")) { throw new CipherException("not_read", null); }
            return input.toUpperCase();
        }

        @Override
        public String encrypt(final String input) {
            return input.toLowerCase();
        }
    }
}