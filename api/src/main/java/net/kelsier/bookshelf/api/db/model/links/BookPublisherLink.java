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

package net.kelsier.bookshelf.api.db.model.links;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.db.model.Entity;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@JsonPropertyOrder({"id", "book", "publisher"})
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class BookPublisherLink implements Entity {
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    @JsonProperty("book")
    @NotNull
    @Min(1)
    private final Integer book;

    @JsonProperty("publisher")
    @NotNull
    @Min(1)
    private final Integer publisher;

    public BookPublisherLink(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                             @JsonProperty("book") @NotNull @Min(1) final Integer book,
                             @JsonProperty("publisher") @NotNull @Min(1) final Integer publisher) {
        this.id = id;
        this.book = book;
        this.publisher = publisher;
    }

    @Override
    public int getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getPublisher() {
        return publisher;
    }
}
