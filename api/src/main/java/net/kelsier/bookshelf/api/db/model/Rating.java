package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Rating details as represented by the rating table
 */
@JsonPropertyOrder({"id", "rating"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Rating implements Entity {
    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Rating value
     */
    @JsonProperty("rating")
    @Min(0)
    @Max(10)
    private final Integer rating;

    /**
     * Constructor used to define a series
     * @param id Unique id
     * @param rating Rating value
     */
    public Rating(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                  @JsonProperty("value") @Min(1)  @Min(10) final Integer rating) {
        this.id = id;
        this.rating = rating;
    }

    /**
     * Unique id
     *
     * @return int containing the unique id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Rating value
     *
     * @return int containing the unique id
     */
    public Integer getRating() {
        return rating;
    }
}
