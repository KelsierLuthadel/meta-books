package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * books_ratings_link (id SERIAL PRIMARY KEY, book INTEGER NOT NULL, rating INTEGER NOT NULL, UNIQUE(book, rating))
 */

public class BookRatingLink {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("rating")
    @Min(1)
    private final Integer rating;

    public BookRatingLink(final Integer id, final Integer book, final Integer rating) {
        this.id = id;
        this.book = book;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getRating() {
        return rating;
    }
}
