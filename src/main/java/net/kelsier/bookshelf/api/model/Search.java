package net.kelsier.bookshelf.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Search {
    @NotNull
    @JsonProperty("field")
    final String field;

    @NotNull
    @JsonProperty("value")
    final String value;

    @Valid
    @NotNull
    @JsonProperty("pagination")
    final Pagination pagination;


    public Search(@JsonProperty("field") final String field,
                  @JsonProperty("value") final String value,
                  @JsonProperty("pagination") final Pagination pagination) {
        this.field = field;
        this.value = value;
        this.pagination = pagination;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
