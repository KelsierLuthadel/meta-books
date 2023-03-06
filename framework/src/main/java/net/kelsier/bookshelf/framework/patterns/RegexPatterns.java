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

package net.kelsier.bookshelf.framework.patterns;

/**
 * This class holds useful Regex Patterns for reuse
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class RegexPatterns {
    /**
     * Regular expression for UUID RFC4122. No /i therefore this is case-sensitive
     */
    public static final String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

    /**
     * Regular expression for RFC 5322 email validation
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    /**
     * Regular expression provided by the OWASP validation regex repository to check the email validation:
     */
    public static final String OWASP_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-] + (?:\\\\.[a-zA-Z0-9_+&*-] + )*@(?:[a-zA-Z0-9-]+\\\\.) + [a-zA-Z]{2, 7}";

    /**
     * Regular expression for password validation:
     *   Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:
     *
     */

    public static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\ \\-\\+\\:\\\\\\/|\\.\\(\\)\\[\\]_^,=;~<>£#$\"'`{}@!%&*?])[\\ \\-\\+\\:\\\\\\/|A-Za-z\\d\\.\\(\\)\\[\\]_^,=;~<>£#\"'`{}$@!%&*?]{8,30}$";

    /**
     * Regular expression for 'No whitespace, at least one character'
     */
    public static final String NO_WHITESPACE_ONE_CHAR_REGEX = "^\\S+$";

    /**
     * Private constructor to hide the implicit public one
     */
    private RegexPatterns() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }
}
