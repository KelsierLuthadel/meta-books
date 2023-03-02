package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
CREATE TABLE books_custom_column_26_link(
                    id SERIAL PRIMARY KEY,
                    book INTEGER NOT NULL,
                    value INTEGER NOT NULL,

                    UNIQUE(book, value)
                    );
 */
public class CustomColumnLink {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    private final Integer book;

    @NotNull
    @JsonProperty("value")
    private final Integer value;

    public CustomColumnLink(final Integer id, final Integer book, final Integer value) {
        this.id = id;
        this.book = book;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getValue() {
        return value;
    }
}
