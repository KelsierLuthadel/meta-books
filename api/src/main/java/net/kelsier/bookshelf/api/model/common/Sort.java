package net.kelsier.bookshelf.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.NotNull;

@Schema(name = "sort", title = "Query sorting", description = "Sort database queries")
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
@JsonPropertyOrder({"field", "direction"})
public class Sort {
    @NotNull
    @JsonProperty("field")
    private final String field;

    @NotNull
    @OneOf({"asc", "desc"})
    @JsonProperty("direction")
    private final String direction;

    public Sort(@JsonProperty("field") final String field, @JsonProperty("direction") final String direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    @JsonProperty("direction")
    @Schema(description = "Search query sort direction", defaultValue = "asc")
    public String getDirection() {
        return direction;
    }
}
