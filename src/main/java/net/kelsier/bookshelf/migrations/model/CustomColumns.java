package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE TABLE custom_columns (
 *     id SERIAL PRIMARY KEY,
 *     label TEXT NOT NULL,
 *     name TEXT NOT NULL,
 *     datatype TEXT NOT NULL,
 *     display  TEXT DEFAULT '{}' NOT NULL,
 *     is_multiple BOOL DEFAULT false NOT NULL,
 *     normalized BOOL NOT NULL,
 *     UNIQUE(label)
 * );
 */
public class CustomColumns {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("label")
    private final String label;

    @NotNull
    @JsonProperty("name")
    private final String name;

    @NotNull
    @JsonProperty("dataType")
    private final String dataType;

    @NotNull
    @JsonProperty("display")
    private final String display;

    @NotNull
    @JsonProperty("multiple")
    private final Boolean multiple;

    @NotNull
    @JsonProperty("normalized")
    private final Boolean normalized;

    public CustomColumns(final Integer id, final String label, final String name, final String dataType,
                         final String display, final Boolean multiple, final Boolean normalized) {
        this.id = id;
        this.label = label;
        this.name = name;
        this.dataType = dataType;
        this.display = display;
        this.multiple = multiple;
        this.normalized = normalized;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDisplay() {
        return display;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public Boolean getNormalized() {
        return normalized;
    }
}
