package net.kelsier.bookshelf.api.model.bookshelf.lookup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.model.common.ColumnLookup;
import net.kelsier.bookshelf.api.model.common.Operator;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;


/**
 * Model for database lookup in the author table.
 *
 */
@Schema(name = "AuthorLookup", title = "Author lookup")
public final class AuthorLookup implements ColumnLookup {
    private static final String DEFAULT_FIELD = "name";
    private static final String DEFAULT_OPERATOR = "LIKE";
    @NotNull
    @JsonProperty("field")
    @OneOf({"name"})
    @Schema(name ="field", description = "Search query field", defaultValue = DEFAULT_FIELD)
    private final String field;

    @NotNull
    @JsonProperty("operator")
    @OneOf({"EQ", "NEQ", "LIKE", "UNLIKE"})
    @Schema(description = "Search operator", defaultValue = DEFAULT_OPERATOR)
    private final Operator operator;

    @NotNull
    @JsonProperty("value")
    private final String value;

    public AuthorLookup(@JsonProperty("field") final String field,
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
