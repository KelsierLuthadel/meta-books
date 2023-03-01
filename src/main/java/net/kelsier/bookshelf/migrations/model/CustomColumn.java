package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
CREATE TABLE custom_column_(
                    id    SERIAL PRIMARY KEY,
                    value TEXT NOT NULL ,
                    UNIQUE(value));
 */
public class CustomColumn {
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

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
