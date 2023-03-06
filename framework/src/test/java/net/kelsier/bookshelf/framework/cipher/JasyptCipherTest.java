package net.kelsier.bookshelf.framework.cipher;


import net.kelsier.bookshelf.framework.encryption.Cipher;
import net.kelsier.bookshelf.framework.encryption.JasyptCipher;
import net.kelsier.bookshelf.framework.encryption.exception.CipherException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class JasyptCipherTest {
    @Test
    public void testEncryptDecrypt() throws CipherException {
        final Cipher cipher = new JasyptCipher("PASSWORD");
        final String testString = "ťėśť_śŧŕỉאַğ";
        assertEquals(testString, cipher.decrypt(cipher.encrypt(testString)));
    }

    @Test
    public void testInvalidDecrypt() {
        final Cipher cipher = new JasyptCipher("PASSWORD");
        final Exception exception = assertThrows(
            CipherException.class,
            () -> cipher.decrypt(cipher.encrypt("test_string") + "blah")
        );
        assertEquals("Error decrypting", exception.getMessage());
    }
}
