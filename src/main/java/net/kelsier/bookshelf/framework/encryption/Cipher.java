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

/**
 * Provides methods for encrypting and decrypting strings.
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public interface Cipher {
    /**
     * Decrypt a string.
     *
     * @param input the data to decrypt
     * @return the decrypted data
     *
     * @throws CipherException when the decryption fails
     */
    String decrypt(String input) throws CipherException;

    /**
     * Encrypt a string.
     *
     * @param input the data to encrypt
     * @return the encrypted data
     *
     * @throws CipherException when the encryption fails
     */
    @SuppressWarnings("RedundantThrows")
    String encrypt(String input) throws CipherException;
}
