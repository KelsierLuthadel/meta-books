package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Identifier implements Entity {
     @NotNull
    @JsonProperty("type")
    private final String type;

    @NotNull
    @JsonProperty("val")
    private final String val;

    public Identifier(final String type, final String val) {
        this.type = type;
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public String getVal() {
        return val;
    }
}
