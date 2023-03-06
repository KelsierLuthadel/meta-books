package net.kelsier.bookshelf.api.model.bookshelf.lookup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.api.model.common.ColumnLookup;
import net.kelsier.bookshelf.api.model.common.Operator;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

/*
 * CREATE TABLE ratings (
 *     id SERIAL PRIMARY KEY,
 *     rating INTEGER CHECK(rating > -1 AND rating < 11),
 *     UNIQUE (rating)
 * );
 */
public class RatingLookup implements ColumnLookup {
    private static final String DEFAULT_FIELD = "rating";
    private static final String DEFAULT_OPERATOR = "EQ";

    @NotNull
    @JsonProperty("field")
    @OneOf({"rating"})
    @Schema(description = "Search query field", defaultValue = DEFAULT_FIELD)
    final String field;

    @NotNull
    @JsonProperty("operator")
    @OneOf({"EQ", "NEQ", "GT", "LT", "GTE", "LTE"})
    @Schema(description = "Search operator", defaultValue = DEFAULT_OPERATOR)
    final Operator operator;

    @NotNull
    @JsonProperty("value")
    final String value;


    public RatingLookup(@JsonProperty("field") final String field,
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
