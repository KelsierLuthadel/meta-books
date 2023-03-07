/*
 * Copyright (c) Kelsier Luthadel 2023.
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
 *
 */

package net.kelsier.bookshelf.framework.config;

import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionConfigurationTest {


    @Test
    void testAlgorithm() {
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 32, 5000, "key")).size(),
            "There should be no violations");
        assertEquals(0,validate(new EncryptionConfiguration("SHA-384", 32, 5000, "key")).size(),
            "There should be no violations");
        assertEquals(0,validate(new EncryptionConfiguration("SHA-512", 32, 5000, "key")).size(),
            "There should be no violations");
    }

    @Test
    void testNullAlgorithm() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration(null, 32, 5000, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testInvalidAlgorithm() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("MD5", 32, 5000, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be one of [SHA-256, SHA-384, SHA-512]",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testSaltSize() {
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 16, 5000, "key")).size(),
            "There should be no violations");
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 32, 5000, "key")).size(),
            "There should be no violations");
    }

    @Test
    void testNullSaltSize() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("SHA-256", null, 5000, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testInvalidSaltSize() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("SHA-256", 99, 5000, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be one of [16, 32]",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testIterations() {
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 16, 5000, "key")).size(),
            "There should be no violations");
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 16, 100_000, "key")).size(),
            "There should be no violations");
    }

    @Test
    void testNullIterations() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("SHA-256", 16, null, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must not be null",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testIterationsSizeMin() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("SHA-256", 16, 4999, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be greater than or equal to 5000",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testIterationsSizeMax() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("SHA-256", 16, 100_001, "key")
        );

        assertEquals(1,violations.size(),"There should be one violation");
        violations.forEach(authorConstraintViolation -> assertEquals("must be less than or equal to 100000",
            authorConstraintViolation.getMessage()));
    }

    @Test
    void testAllBad() {
        final Set<ConstraintViolation<Object>> violations = validate(
            new EncryptionConfiguration("SHA-257", 33, 1, "key")
        );

        assertEquals(3,violations.size(),"There should be one violation");
    }

    @Test
    void testKey() {
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 16, 5000, "any key")).size(),
            "There should be no violations");
        assertEquals(0,validate(new EncryptionConfiguration("SHA-256", 16, 5000, null)).size(),
            "There should be no violations");
    }

    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}