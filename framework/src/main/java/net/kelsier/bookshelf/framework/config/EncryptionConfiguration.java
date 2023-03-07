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

package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.dropwizard.validation.OneOf;
import net.kelsier.bookshelf.framework.encryption.Encrypted;
import net.kelsier.bookshelf.framework.validator.IntegerOneOf;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Configuration for authentication
 * <p>
 * Only basic authentication is supported at the moment
 * </p>
 * The following config parameters control the behavior of authentication
 * <dl>
 * <dt>authType</dt>
 * <dd>The authentication type (only basic is supported)
 * </dd>
 * <dt>Realm</dt>
 * <dd> The realm
 * </dd>
 * </dl>
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public final class EncryptionConfiguration {

    /**
     * Algorithm used for encryption.
     * This must be one of: SHA-256, SHA-384, SHA-512
     */
    @JsonSetter(value = "algorithm", nulls = Nulls.SKIP)
    @OneOf({"SHA-256", "SHA-384", "SHA-512"})
    @NotNull
    private final String algorithm;

    /**
     * Salt size used for encryption
     * This must be one of: 16, 32
     */
    @JsonSetter(value = "saltSize", nulls = Nulls.SKIP)
    @NotNull
    @IntegerOneOf({16, 32})
    private final Integer saltSize;

    /**
     * Number of iterations used for encryption
     * This must be between 5,000 and 100,000
     */
    @JsonSetter(value = "iterations", nulls = Nulls.SKIP)
    @NotNull
    @Min(5_000)
    @Max(100_000)
    private final Integer iterations;

    /**
     * Key used for encryption
     * Note: This is not implemented
     */
    @JsonSetter(value = "key", nulls = Nulls.SKIP)
    @Nullable
    @Encrypted
    private final String key;


    /**
     * @param algorithm - Algorithm used for encryption
     * @param saltSize - Salt size used for encryption
     * @param iterations - Number of iterations used for encryption
     * @param key - Key used for encryption

     */
    @JsonCreator
    public EncryptionConfiguration(@JsonProperty("algorithm") final String algorithm,
                                   @JsonProperty("saltSize")final Integer saltSize,
                                   @JsonProperty("iterations")final Integer iterations,
                                   @JsonProperty("key") final String key) {
        this.algorithm = algorithm;
        this.saltSize = saltSize;
        this.iterations = iterations;
        this.key = key;
    }

    /**
     * Algorithm used for encryption.
     * @return - String representing the algorithm, this will be one of: SHA-256, SHA-384, SHA-512
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Salt size used for encryption
     * @return - int representing the salt size, this will be one of: 16, 32
     */
    public int getSaltSize() {
        return saltSize;
    }

    /**
     * Number of iterations used for encryption
     * @return - int representing the number of iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Encryption key
     *
     * @return String representing the encryption key, note: this is not implemented
     */
    public String getKey() {
        return key;
    }

}
