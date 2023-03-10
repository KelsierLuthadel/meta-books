package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
books_custom_column_n_link(id SERIAL PRIMARY KEY, book INTEGER NOT NULL, value INTEGER NOT NULL, UNIQUE(book, value))
 */

public class CustomColumnLink implements Entity {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @Min(1)
    @JsonProperty("book")
    private final Integer book;

    @NotNull
    @Min(1)
    @JsonProperty("value")
    private final Integer value;

    public CustomColumnLink(final Integer id, final Integer book, final Integer value) {
        this.id = id;
        this.book = book;
        this.value = value;
    }

    @Override
    public int getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getValue() {
        return value;
    }
}
