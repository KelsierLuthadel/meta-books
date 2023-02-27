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

package net.kelsier.bookshelf.framework.log;

/**
 * Use ANSI colours in log output
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public enum LogColour {
    /**
     *  Ansi reset value
     */
    ANSI_RESET("\u001B[0m"),
    /**
     * Ansi colour Red
     */
    ANSI_RED("\u001B[31m"),
    /**
     * Ansi colour Green
     */
    ANSI_GREEN("\u001B[32m"),
    /**
     * Ansi colour Blue
     */
    ANSI_BLUE("\u001B[34m"),
    /**
     * Ansi colour Yellow
     */
    ANSI_YELLOW("\u001B[33m"),
    /**
     * Ansi colour Purple
     */
    ANSI_PURPLE("\u001B[35m"),
    /**
     * Ansi colour Cyan
     */
    ANSI_CYAN("\u001B[36m");


    private final String value;

    /**
     * Constructor
     *
     * @param value An ansi colour value
     */
    LogColour(final String value) {
        this.value = value;
    }

    /**
     * Get the log colour value
     *
     * @return A string containing the ANSI colour value
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the log colour value
     *
     * @return A string containing the ANSI colour value
     */
    @Override
    public String toString() {
        return getValue();
    }
}
