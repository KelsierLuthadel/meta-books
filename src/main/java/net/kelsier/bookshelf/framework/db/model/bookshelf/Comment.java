package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    text TEXT NOT NULL ,
    UNIQUE(book)
);
 */
public class Comment {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @JsonProperty("text")
    private final String text;

    public Comment(final Integer id, final Integer book, final String text) {
        this.id = id;
        this.book = book;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public String getText() {
        return text;
    }
}
