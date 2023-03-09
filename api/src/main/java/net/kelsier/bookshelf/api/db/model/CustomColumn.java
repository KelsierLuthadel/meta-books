package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 custom_column_n(id SERIAL PRIMARY KEY, value TEXT NOT NULL, UNIQUE(value))
 */

public class CustomColumn implements Entity {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("value")
    private final String value;


    public CustomColumn(final Integer id, final String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
