package net.kelsier.bookshelf.framework.loaders;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.kelsier.bookshelf.framework.encryption.Cipher;
import net.kelsier.bookshelf.framework.encryption.Encrypted;
import net.kelsier.bookshelf.framework.encryption.exception.CipherException;

import java.io.IOException;

/**
 * This class defines a jackson module which, when registered to an object mapper, adds support for the encryption annotation
 */
public final class EncryptedModule extends SimpleModule {
    private static final long serialVersionUID = 1L;
    private final transient Cipher cipher;

    /**
     * Constructor
     *
     * @param cipher Takes a {@link Cipher} to use for decryption
     */
    EncryptedModule(final Cipher cipher) {
        this.cipher = cipher;
    }

    /**
     * Takes a SetupContext (interface for extending ObjectMapper functionality) and adds the {@link EncryptedAnnotationIntrospector}
     * so the ObjectMapper gains the ability to decrypt encrypted fields
     *
     * @param context - Instance of SetupContext Jackson interface
     */
    @Override
    public void setupModule(final SetupContext context) {
        context.appendAnnotationIntrospector(new EncryptedAnnotationIntrospector());
    }

    /**
     * Determines the fields within the configuration that have the encrypted annotation. If an encrypted field is
     * found then the {@link EncryptedDeserializer} is used to deserialize the field, otherwise the default deserializer
     * (YAML) will be used.
     */
    public class EncryptedAnnotationIntrospector extends NopAnnotationIntrospector {
        private static final long serialVersionUID = 1L;

        @Override
        public Object findDeserializer(final Annotated am) {
            if (am.hasAnnotation(Encrypted.class)) {
                return new EncryptedDeserializer(am.getRawType());
            }
            return super.findDeserializer(am);
        }
    }

    /**
     * This class is a custom deserializer which is able to deserialize encrypted values
     */
    class EncryptedDeserializer extends JsonDeserializer {
        private final Class<?> rawType;

        /**
         * Constructor
         *
         * @param rawType - The expected field type
         */
        private EncryptedDeserializer(final Class<?> rawType) { this.rawType = rawType; }

        /**
         * Implementation of the deserialize function. Takes the value of a field with the encrypted annotation and
         * attempts to deserialize it. If the type of the field is not a String then it should not have the encrypted
         * annotation and hence an error is thrown.
         *
         * @param p       - An interface for reading content
         * @param context - Context for the process of deserialization, contains settings and other high level components
         * @return The deserialized, decrypted value for the configuration
         * @throws IOException Thrown if the expected type was not a string or if an error occurred whilst decrypting
         */
        @Override
        public String deserialize(final JsonParser p, final DeserializationContext context) throws IOException {
            if (!rawType.equals(String.class)) {
                throw new IOException("Encrypted annotation should not be added to non-String fields");
            }
            try {
                return cipher.decrypt(p.getValueAsString());
            } catch (final CipherException e) {
                throw new IOException("Error decrypting encrypted field", e);
            }
        }
    }
}