package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * identifiers (id SERIAL PRIMARY KEY, book INTEGER NOT NULL, type TEXT NOT NULL DEFAULT 'isbn', val TEXT NOT NULL, UNIQUE(book, type))
 */

@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Identifier implements Entity {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("type")
    private final String type;

    @NotNull
    @JsonProperty("val")
    private final String val;

    public Identifier(final Integer id, final Integer book, final String type, final String val) {
        this.id = id;
        this.book = book;
        this.type = type;
        this.val = val;
    }

    @Override
    public int getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public String getType() {
        return type;
    }

    public String getVal() {
        return val;
    }
}
