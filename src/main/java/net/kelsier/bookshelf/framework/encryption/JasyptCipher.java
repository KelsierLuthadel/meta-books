/*
 * Copyright (c) 2023 Kelsier Luthadel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kelsier.bookshelf.framework.encryption;

import net.kelsier.bookshelf.framework.encryption.exception.CipherException;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * Implementation of a SecurityProvider that uses Jasypt to provide basic
 * text encryption/decryption capabilities.
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class JasyptCipher implements Cipher {
    /**
     * This is PBE With MD5 And Triple DES
     */
    private final StrongTextEncryptor codec = new StrongTextEncryptor();

    /**
     * Constructor
     *
     * @param configCipherPass This is the keyword used to encrypt and decrypt data.
     */
    public JasyptCipher(final String configCipherPass) {
        codec.setPassword(configCipherPass);
    }

    /**
     * Called with some cipher text, returns plain text
     *
     * @param input the data to decrypt
     * @return the decrypted data
     */
    @Override
    public String decrypt(final String input) throws CipherException {
        try {
            return codec.decrypt(input);
        } catch (final Exception e) {
            throw new CipherException("Error decrypting", e);
        }
    }

    /**
     * Called with some plain text, returns cipher text
     *
     * @param input the data to encrypt
     * @return the encrypted data
     */
    @Override
    public String encrypt(final String input) {
        return codec.encrypt(input);
    }
}
