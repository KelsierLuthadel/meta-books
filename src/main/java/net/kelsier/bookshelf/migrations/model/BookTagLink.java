package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 id SERIAL PRIMARY KEY,
 book INTEGER NOT NULL,
 tag INTEGER NOT NULL,
 UNIQUE(book, tag)
 );
 */
public class BookTagLink {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("tag")
    @Min(1)
    private final Integer tag;

    public BookTagLink(final Integer id, final Integer book, final Integer tag) {
        this.id = id;
        this.book = book;
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getTag() {
        return tag;
    }
}
