package net.kelsier.bookshelf.framework.cipher;

import net.kelsier.bookshelf.framework.encryption.exception.CipherException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CipherExceptionTest {
    @Test
   public void testCipherException() {
        final String message = "exception_message";
        final Throwable cause = new Throwable("cause");
        final CipherException exception = new CipherException(message, cause);

        assertEquals(exception.getMessage(), message);
        assertEquals(exception.getCause(), cause);
    }
}
