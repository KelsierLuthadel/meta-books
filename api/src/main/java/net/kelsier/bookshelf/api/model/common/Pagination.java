package net.kelsier.bookshelf.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Schema(name = "pagination", title = "Response pagination", description = "Restrict database query size")
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
@JsonPropertyOrder({"start", "offset", "order"})
public class Pagination {
    @NotNull
    @JsonProperty("start")
    @Min(0)
    private final Integer start;

    @NotNull
    @JsonProperty("limit")
    @Min(0)
    @Max(100)
    private final Integer limit;

    @Valid
    @JsonProperty("sort")
    private final Sort sort;

    public Pagination(@JsonProperty("start") final Integer start,
                      @JsonProperty("limit") final Integer limit,
                      @JsonProperty("sort") final Sort sort) {
        this.start = start;
        this.limit = limit;

        this.sort = Objects.requireNonNullElseGet(sort, () -> new Sort("id", "asc"));
    }

    public Integer getStart() {
        return start;
    }

    @JsonProperty("limit")
    @Schema(description = "Pagination result limit", defaultValue = "10")
    public Integer getLimit() {
        return limit;
    }

    public Sort getSort() {
        return sort;
    }
}
