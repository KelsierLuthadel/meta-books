package net.kelsier.bookshelf.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.NotNull;

/**
 * Sort parameters used when making a database query
 */
@Schema(name = "sort", title = "Query sorting", description = "Sort database queries")
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
@JsonPropertyOrder({"field", "direction"})
public class Sort {

    /**
     * The colum used for sorting data
     */
    @NotNull
    @JsonProperty("field")
    private final String field;

    /**
     * The sort direction, this can be one of: asc, desc
     */
    @NotNull
    @OneOf({"asc", "desc"})
    @JsonProperty("direction")
    private final String direction;

    /**
     * Constructor used for supplying sort criteria
     *
     * @param field Column used for sorting data
     * @param direction Sort direction, this can be one of: asc, desc
     */
    public Sort(@JsonProperty("field") final String field, @JsonProperty("direction") final String direction) {
        this.field = field;
        this.direction = direction;
    }

    /**
     * Sort field
     *
     * @return string containing the column used for sorting
     */
    public String getField() {
        return field;
    }

    /**
     * Sort direction
     *
     * @return string contining the sort direction, this can be one of: asc, desc
     */
    @JsonProperty("direction")
    @Schema(description = "Search query sort direction", defaultValue = "asc")
    public String getDirection() {
        return direction;
    }
}
