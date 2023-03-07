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

/**
 * Pagination used to limit the number of rows returned in a database query
 */
@Schema(name = "pagination", title = "Response pagination", description = "Restrict database query size")
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
@JsonPropertyOrder({"start", "offset", "order"})
public class Pagination {

    /**
     * Default pagination limit that will be used in the OpenAPI example
     */
    private static final String DEFAULT_PAGINATION_LIMIT = "10";

    /**
     * Pagination start position
     * This is a zero-based offset
     */
    @NotNull
    @JsonProperty("start")
    @Min(0)
    private final Integer start;

    /**
     * Pagination limit
     * This is a zero based limit with a maximum value of 100
     */
    @NotNull
    @JsonProperty("limit")
    @Min(0)
    @Max(100)
    private final Integer limit;

    /**
     * Sorting parameters
     */
    @Valid
    @JsonProperty("sort")
    private final Sort sort;

    /**
     * Constructor used for supplying pagination information
     *
     * @param start Pagination start position
     * @param limit Pagination limit
     * @param sort Sorting parameters
     */
    public Pagination(@JsonProperty("start") final Integer start,
                      @JsonProperty("limit") final Integer limit,
                      @JsonProperty("sort") final Sort sort) {
        this.start = start;
        this.limit = limit;

        this.sort = Objects.requireNonNullElseGet(sort, () -> new Sort("id", "asc"));
    }

    /**
     * Pagination start
     *
     * @return int representing the pagination start position
     */
    public int getStart() {
        return start;
    }

    /**
     * Pagination limit
     *
     * @return int representing the maximum number of items to return
     */
    @JsonProperty("limit")
    @Schema(description = "Pagination result limit", defaultValue = DEFAULT_PAGINATION_LIMIT)
    public int getLimit() {
        return limit;
    }

    /**
     * Sorting parameters
     *
     * @return a Sort object containing sorting parameters
     */
    public Sort getSort() {
        return sort;
    }
}
