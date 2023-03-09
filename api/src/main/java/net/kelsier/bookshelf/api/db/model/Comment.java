package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *Comment details as represented by the comments table
 */
@JsonPropertyOrder({"id", "book", "text"})
public class Comment implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Foreign key for linked book
     */
    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    /**
     * Comments
     */
    @NotNull
    @JsonProperty("text")
    private final String text;

    /**
     * Constructor used to define a book
     * @param id Unique id
     * @param book Foreign key for linked book
     * @param text Comments
     */
    public Comment(@JsonProperty("id") final Integer id, @JsonProperty("book") final Integer book, @JsonProperty("text") final String text) {
        this.id = id;
        this.book = book;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public String getText() {
        return text;
    }
}
