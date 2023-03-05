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
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL DEFAULT 'Unknown',
    sort TEXT ,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    publication_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    series_index REAL NOT NULL DEFAULT 1.0,
    isbn TEXT DEFAULT '' ,
    path TEXT NOT NULL DEFAULT '',
    has_cover BOOL DEFAULT false,
    last_modified TIMESTAMP NOT NULL DEFAULT '2000-01-01 00:00:00+00:00'
)
 */

public class BookLookup implements ColumnLookup {
    private static final String DEFAULT_FIELD = "title";
    private static final String DEFAULT_OPERATOR = "LIKE";

    @NotNull
    @JsonProperty("field")
    @OneOf({"title", "isbn", "has_cover"})
    @Schema(description = "Search query field", defaultValue = DEFAULT_FIELD)
    final String field;

    @NotNull
    @JsonProperty("operator")
    @OneOf({"EQ", "NEQ", "GT", "LT", "GTE", "LTE", "LIKE", "UNLIKE"})
    @Schema(description = "Search operator", defaultValue = DEFAULT_OPERATOR)
    final Operator operator;

    @NotNull
    @JsonProperty("value")
    final String value;

    public BookLookup(@JsonProperty("field") final String field,
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
