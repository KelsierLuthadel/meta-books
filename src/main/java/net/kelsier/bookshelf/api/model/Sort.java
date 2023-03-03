package net.kelsier.bookshelf.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dropwizard.validation.OneOf;

import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"field", "direction"})
public class Sort {
    @NotNull
    @JsonProperty("field")
    final String field;

    @NotNull
    @OneOf({"asc", "desc"})
    @JsonProperty("direction")
    final String direction;

    public Sort(@JsonProperty("field") final String field, @JsonProperty("direction") final String direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public String getDirection() {
        return direction;
    }
}
