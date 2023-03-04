package net.kelsier.bookshelf.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.framework.filter.EmptyValueFilter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Schema(name = "search", title = "Database query", description = "Search database for entities")
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Search<T> {
    @Valid
//    final Lookup lookup;
    final T lookup;

    @Valid
    @NotNull
    @JsonProperty("pagination")
    final Pagination pagination;


    public Search(@JsonProperty("lookup") final T lookup,
                  @JsonProperty("pagination") final Pagination pagination) {
        this.lookup = lookup;
        this.pagination = pagination;
    }


    public T getLookup() {
        return lookup;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
