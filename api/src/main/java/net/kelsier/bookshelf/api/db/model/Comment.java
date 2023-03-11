package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *Comment details as represented by the comments table
 */
@JsonPropertyOrder({"id", "book", "text"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Comment implements Entity {
    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Foreign key for linked book
     */
    @JsonProperty("book")
    @NotNull
    @Min(1)
    private final Integer book;

    /**
     * Comments
     */
    @JsonProperty("text")
    @NotNull
    private final String text;

    /**
     * Constructor used to define a book
     * @param id Unique id
     * @param book Foreign key for linked book
     * @param text Comments
     */
    public Comment(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                   @JsonProperty("book") @NotNull @Min(1) final Integer book,
                   @JsonProperty("text") @NotNull @Min(1) final String text) {
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
