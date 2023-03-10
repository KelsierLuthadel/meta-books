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

package net.kelsier.bookshelf.api.db.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Book metadata consisting of data pulled from identifiers table
 */
@JsonPropertyOrder({"id", "title", "author", "series", "seriesIndex", "publisher", "isbn", "type", "value",
    "language", "format","size", "hasCover", "dateAdded", "publicationDate", "lastModified", "path", "comments"})
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Identifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(Identifier.class);
    /**
     * List of book identifiers (isbn, google, goodreads, amazon etc)
     */
    @JsonProperty("type")
    private final List<String> identifierTypes;

    /**
     * List of values associated with book identifiers
     */
    @JsonProperty("value")
    private final List<String> identifierValues;

    /**
     * Constructor used to define book identifiers. This object represents a list of possible book identifiers such as ISBN, Goodreads,
     * Amazon etc.
     * @param identifierTypes List of providers such as ISBN, Hoodreads, Amazon etc
     * @param identifierValues List of provider lookup values, such as ISBN
     */
    public Identifier(@JsonProperty("type") final String[] identifierTypes,
                      @JsonProperty("value") final String[] identifierValues) {

        this.identifierTypes = Arrays.asList(identifierTypes);
        this.identifierValues = Arrays.asList(identifierValues);
    }


    public List<String> getIdentifierTypes() {
        return identifierTypes;
    }

    public List<String> getIdentifierValues() {
        return identifierValues;
    }
}
