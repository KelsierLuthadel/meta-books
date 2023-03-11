package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@JsonPropertyOrder({"id", "book", "type", "val"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Identifier implements Entity {

    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    @JsonProperty("book")
    @NotNull
    @Min(1)
    private final Integer book;

    @JsonProperty("type")
    @NotNull
    private final String type;

    @JsonProperty("val")
    @NotNull
    private final String val;

    public Identifier(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                      @JsonProperty("book") @NotNull final Integer book,
                      @JsonProperty("type") @NotNull final String type,
                      @JsonProperty("val") @NotNull final String val) {
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
