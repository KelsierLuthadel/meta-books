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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kelsier.bookshelf.framework.filter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Map;

/**
 * Jackson Filter to remove empty objects from the response
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public class EmptyValueFilter {
    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 1415).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return true;
        }

        if (other instanceof String) {
            return ((String) other).isEmpty();
        }
        if (other instanceof  Map) {
            return ((Map<?,?>) other).size() == 0;
        }

        if (other instanceof Collection) {
            return ((Collection<?>) other).isEmpty();
        }

        return false;
    }
}
