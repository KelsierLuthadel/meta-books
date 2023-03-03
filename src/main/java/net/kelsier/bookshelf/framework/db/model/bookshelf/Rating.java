package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE TABLE ratings (
 *     id SERIAL PRIMARY KEY,
 *     rating INTEGER CHECK(rating > -1 AND rating < 11),
 *     UNIQUE (rating)
 * );
 */
public class Rating {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @JsonProperty("rating")
    @Min(0)
    @Max(10)
    private final Integer rating;

    public Rating(final Integer id, final Integer rating) {
        this.id = id;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }
}
