package net.kelsier.bookshelf.api.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.model.common.ColumnLookup;
import net.kelsier.bookshelf.api.model.common.Operator;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

/*
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    text TEXT NOT NULL ,
    UNIQUE(book)
);
 */
public class CommentLookup implements ColumnLookup {
   private static final String DEFAULT_FIELD = "text";
    private static final String DEFAULT_OPERATOR = "LIKE";

    @NotNull
    @JsonProperty("field")
    @Schema(description = "Search query field", defaultValue = DEFAULT_FIELD)
    @OneOf({"text"})
    final String field;

    @NotNull
    @JsonProperty("operator")
    @OneOf({"EQ", "NEQ", "LIKE", "UNLIKE"})
    @Schema(description = "Search operator", defaultValue = DEFAULT_OPERATOR)
    final Operator operator;

    @NotNull
    @JsonProperty("value")
    final String value;

    public CommentLookup(@JsonProperty("field") final String field,
                         @JsonProperty("operator") final Operator operator,
                         @JsonProperty("value") final String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getValue() {
        return value;
    }

    public String getLookupValue() {
        if (Operator.LIKE == operator || Operator.UNLIKE == operator) {
            return getWildcardValue();
        }

        return value;
    }

    @JsonIgnore
    private String getWildcardValue() {
        return MessageFormat.format("%{0}%", value);
    }
}
