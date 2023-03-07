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
 * Model for database lookup in the series table.
 *
 */
public class SeriesLookup implements ColumnLookup {
    /**
     * Default column lookup that will be used in the OpenAPI example
     */
    private static final String DEFAULT_FIELD = "name";

    /**
     * Default search constraint that will be used in the OpenAPI example
     */
    private static final String DEFAULT_OPERATOR = "LIKE";

    /**
     * Search query field mapped to a database column
     */
    @NotNull
    @JsonProperty("field")
    @OneOf({"name"})
    @Schema(description = "Search query field", defaultValue = DEFAULT_FIELD)
    private final String field;

    /**
     * Allowed search operators
     */
    @NotNull
    @JsonProperty("operator")
    @OneOf({"EQ", "NEQ", "LIKE", "UNLIKE"})
    @Schema(description = "Search operator", defaultValue = DEFAULT_OPERATOR)
    private final Operator operator;

    /**
     * Value used for a database query
     */
    @NotNull
    @JsonProperty("value")
    private final String value;

    /**
     * Constructor used to perform a search query on a database table
     *
     * @param field Query column
     * @param operator Query operator
     * @param value Query value
     */
    public SeriesLookup(@JsonProperty("field") final String field,
                        @JsonProperty("operator") final Operator operator,
                        @JsonProperty("value") final String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Query column
     *
     * @return String containing the column used in a query
     */
    @Override
    public String getField() {
        return field;
    }

    /**
     * Query operator
     *
     * @return String containing the operator used in a query
     */
    @Override
    public Operator getOperator() {
        return operator;
    }

    /**
     * Query value
     *
     * @return String containing the value used in a query
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Database lookup value
     *
     * @return String containing the value used in a query. If the lookup requires a wildcard, this value will contain the wildcard
     */
    @JsonIgnore
    public String getLookupValue() {
        if (Operator.LIKE == operator || Operator.UNLIKE == operator) {
            return getWildcardValue();
        }

        return value;
    }

    /**
     * Apply wildcard to query
     *
     * @return String containing the initial query value with a wildcard
     */
    @JsonIgnore
    private String getWildcardValue() {
        return MessageFormat.format("%{0}%", value);
    }
}
