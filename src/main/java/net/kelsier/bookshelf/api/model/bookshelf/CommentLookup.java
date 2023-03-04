package net.kelsier.bookshelf.api.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

public class CommentLookup {
    @NotNull
    @JsonProperty("field")
    @OneOf({"text"})
    final String field;

    @NotNull
    @JsonProperty("value")
    final String value;


    public CommentLookup(@JsonProperty("field") final String field,
                         @JsonProperty("value") final String value) {
        this.field = field;
        this.value = value;
    }

    @JsonProperty("field")
    @Schema(description = "Search query field", defaultValue = "name")
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
