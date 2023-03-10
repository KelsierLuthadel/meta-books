package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * books_publishers_link (id INTEGER PRIMARY KEY, book INTEGER NOT NULL, publisher INTEGER NOT NULL, UNIQUE(book))
 */

public class BookPublisherLink implements Entity {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("publisher")
    @Min(1)
    private final Integer publisher;

    public BookPublisherLink(final Integer id, final Integer book, final Integer publisher) {
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
