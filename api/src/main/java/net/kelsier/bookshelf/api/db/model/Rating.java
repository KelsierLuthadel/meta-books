package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * ratings (id SERIAL PRIMARY KEY, rating INTEGER CHECK(rating > -1 AND rating < 11), UNIQUE (rating))
 */

public class Rating implements Entity {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @JsonProperty("rating")
    @Min(0)
    @Max(10)
    private final Integer value;

    public Rating(@JsonProperty("id") final Integer id, @JsonProperty("rating") final Integer value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }
}
