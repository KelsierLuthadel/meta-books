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
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
@JsonPropertyOrder({"id", "rating"})
public class Rating implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Rating value
     */
    @JsonProperty("rating")
    @Min(0)
    @Max(10)
    private final Integer value;

    /**
     * Constructor used to define a series
     * @param id Unique id
     * @param value Rating value
     */
    public Rating(@JsonProperty("id") final Integer id, @JsonProperty("rating") final Integer value) {
        this.id = id;
        this.value = value;
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
    public Integer getValue() {
        return value;
    }
}
