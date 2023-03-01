package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE TABLE books_authors_link (
 *     id SERIAL PRIMARY KEY,
 *     book INTEGER NOT NULL,
 *     author INTEGER NOT NULL,
 *     UNIQUE(book, author)
 * );
 */
public class BookAuthorLink {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("author")
    @Min(1)
    private final Integer author;

    public BookAuthorLink(final Integer id, final Integer book, final Integer author) {
        this.id = id;
        this.book = book;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getAuthor() {
        return author;
    }
}
