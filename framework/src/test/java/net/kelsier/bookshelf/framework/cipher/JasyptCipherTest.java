package net.kelsier.bookshelf.framework.cipher;


import net.kelsier.bookshelf.framework.encryption.Cipher;
import net.kelsier.bookshelf.framework.encryption.JasyptCipher;
import net.kelsier.bookshelf.framework.encryption.exception.CipherException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JasyptCipherTest {
    @Test
    void testEncryptDecrypt() throws CipherException {
        final Cipher cipher = new JasyptCipher("PASSWORD");
        final String testString = "ťėśť_śŧŕỉאַğ";
        assertEquals(testString, cipher.decrypt(cipher.encrypt(testString)));
    }

    @Test
    void testInvalidDecrypt() {
        final Cipher cipher = new JasyptCipher("PASSWORD");
        final Exception exception = assertThrows(
            CipherException.class,
            () -> cipher.decrypt(cipher.encrypt("test_string") + "blah")
        );
        assertEquals("Error decrypting", exception.getMessage());
    }
}
