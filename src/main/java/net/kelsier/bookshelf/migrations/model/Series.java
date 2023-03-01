package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE TABLE series (
 *     id SERIAL PRIMARY KEY,
 *     name TEXT NOT NULL ,
 *     sort TEXT ,
 *     UNIQUE (name)
 * );
 */
public class Series {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("name")
    private final String name;

    @JsonProperty("text")
    private final String text;

    public Series(final Integer id, final String name, final String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
