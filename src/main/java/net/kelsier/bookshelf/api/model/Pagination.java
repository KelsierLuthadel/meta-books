package net.kelsier.bookshelf.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"start", "offset", "order"})
public class Pagination {
    @NotNull
    @JsonProperty("start")
    final Integer start;

    @NotNull
    @JsonProperty("limit")
    final Integer limit;

    @Valid
    @JsonProperty("sort")
    final Sort sort;

    public Pagination(@JsonProperty("start") final Integer start,
                      @JsonProperty("limit") final Integer limit,
                      @JsonProperty("sort") final Sort sort) {
        this.start = start;
        this.limit = limit;
        this.sort = sort;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getLimit() {
        return limit;
    }

    public Sort getSort() {
        return sort;
    }
}
