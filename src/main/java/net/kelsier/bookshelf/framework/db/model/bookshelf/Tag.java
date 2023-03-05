package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * tags(id SERIAL PRIMARY KEY, name TEXT NOT NULL , UNIQUE (name))
 */

public class Tag {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("name")
    private final String name;

    public Tag(@NotNull @JsonProperty("id") @Min(1) final Integer id,
               @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
