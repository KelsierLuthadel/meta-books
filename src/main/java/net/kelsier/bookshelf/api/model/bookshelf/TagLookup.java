package net.kelsier.bookshelf.api.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.model.common.ColumnLookup;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

public final class TagLookup implements ColumnLookup {
    private static final String DEFAULT_VALUE = "name";
    @NotNull
    @JsonProperty("field")
    @OneOf({"name"})
    @Schema(name ="field", description = "Search query field", defaultValue = DEFAULT_VALUE)
    final String field;

    @NotNull
    @JsonProperty("value")
    final String value;


    public TagLookup(@JsonProperty("field") final String field,
                     @JsonProperty("value") final String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
    @JsonIgnore
    public String getWildcardValue() {
        return MessageFormat.format("%{0}%", getValue());
    }

}
