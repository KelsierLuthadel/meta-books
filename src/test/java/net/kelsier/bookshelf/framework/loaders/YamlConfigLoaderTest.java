package net.kelsier.bookshelf.framework.loaders;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import net.kelsier.bookshelf.framework.config.exception.ConfigurationException;
import net.kelsier.bookshelf.framework.encryption.Cipher;
import net.kelsier.bookshelf.framework.encryption.Encrypted;
import net.kelsier.bookshelf.framework.encryption.JasyptCipher;
import net.kelsier.bookshelf.framework.encryption.exception.CipherException;
import org.junit.jupiter.api.Test;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
class YamlConfigLoaderTest {
    @Test
    void testValidReadConfiguration() throws ConfigurationException {
        final ValidRead configuration = loadTestConfiguration(ValidRead.class);
        assertEquals(5, configuration.testInteger, "testInteger value should match");
        assertEquals("STR", configuration.testString, "testString value should match");
    }

    @Test
    void testInvalidReadConfiguration() {
        assertThrows(ConfigurationException.class,
            () -> loadTestConfiguration(InvalidRead.class),
            "A ConfigurationException exception should be thrown");
    }

    @Test
    void testCasting() throws  ConfigurationException {
        final Casting configuration = loadTestConfiguration(Casting.class);
        assertEquals(5, configuration.testInteger, "testInteger value should match");
        assertEquals("5", configuration.testString, "testString value should match");
    }

    @Test
    void testReadConfigurationNested() throws  ConfigurationException {
        final Outer configuration = loadTestConfiguration(Outer.class);
        assertEquals(5, configuration.testInteger, "Integer value should match");
        assertEquals("STR", configuration.testString, "String value should match");
        assertEquals("STR", configuration.testInner.testString, "testInner value should match");
    }

    @Test
    void testEncryptedValues() throws ConfigurationException {
        final JasyptCipher cipher = new JasyptCipher("xyzzy");

        final EncryptedValues values = new YamlConfigLoader("src/test/resources/config/", cipher).
            loadConfiguration(EncryptedValues.class);

        assertEquals("EncryptedUser", values.getUsername(), "Username should be decrypted");
        assertEquals("EncryptedPassword", values.getPassword(), "Password should be decrypted");
        assertEquals("Plaintext", values.getPlaintext(), "Plaintext should be plaintext");
        assertEquals("StringNotEncrypted", values.getNotEncrypted(), "Encrypted field should contain plaintext");
    }

    @Test
    void testEncryptedValuesWrongKey() throws ConfigurationException {
        final JasyptCipher cipher = new JasyptCipher("badkey");

        final EncryptedValues values = new YamlConfigLoader("src/test/resources/config/", cipher).
            loadConfiguration(EncryptedValues.class);

        assertEquals("UUGoqeYjd4Z77etPtQPSyTFf9gxIRZ2H", values.getUsername(), "Username should remain encrypted");
        assertEquals("VbV7GIcCnrm8fbhJMkjeTn6+hiKIp+HSQXEx9skshNA=", values.getPassword(), "Password remain encrypted");
        assertEquals("Plaintext", values.getPlaintext(), "Plaintext should be plaintext");
        assertEquals("StringNotEncrypted", values.getNotEncrypted(), "Encrypted field should contain plaintext");
    }

    private static final class EncryptedValues {
        @NotNull
        @Encrypted
        private String username;

        @NotNull
        @Encrypted
        private String password;

        @NotNull
        private String plaintext;

        @NotNull
        @Encrypted
        private String notEncrypted;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getPlaintext() {
            return plaintext;
        }

        public String getNotEncrypted() {
            return notEncrypted;
        }
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