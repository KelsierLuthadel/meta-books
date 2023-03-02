package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
CREATE TABLE public.authors (
   id SERIAL PRIMARY KEY,
   name TEXT NOT NULL ,
   sort TEXT
);
 */
public class Author {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("name")
    private final String name;

    @NotNull
    @JsonProperty("sort")
    private final String sort;

    public Author(final Integer id, final String name, final String sort) {
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
