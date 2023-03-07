package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * series(id SERIAL PRIMARY KEY, name TEXT NOT NULL, sort TEXT, UNIQUE (name))
 */

public class Series {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("name")
    private final String name;

    @JsonProperty("sort")
    private final String sort;

    public Series(@JsonProperty("id") final Integer id, @JsonProperty("name") final String name, @JsonProperty("sort") final String sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSort() {
        return sort;
    }
}