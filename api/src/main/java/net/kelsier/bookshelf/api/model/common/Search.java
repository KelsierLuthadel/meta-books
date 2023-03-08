package net.kelsier.bookshelf.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Search parameters containing a query and pagination
 *
 * @param <T> an instance of {@link ColumnLookup} for specific query data
 */
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public final class Search<T> {

    /**
     * Search query
     */
    @Valid
    private final T query;

    /**
     * Query pagination and sort criteria
     */
    @Valid
    @NotNull
    @JsonProperty("pagination")
    private final Pagination pagination;

    /**
     * Constructor used for supplying a database query
     *
     * @param query Search query
     * @param pagination Query pagination and sort criteria
     */
    public Search(@JsonProperty("query") final T query,
                  @JsonProperty("pagination") final Pagination pagination) {
        this.query = query;
        this.pagination = pagination;
    }

    /**
     * Search query
     *
     * @return Object representing the search query
     */
    public T getQuery() {
        return query;
    }

    /**
     * Query pagination
     *
     * @return Pagination and sort information
     */
    public Pagination getPagination() {
        return pagination;
    }
}
