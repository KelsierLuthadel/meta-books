package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Publisher details as represented by the publishers table
 */
@JsonPropertyOrder({"id", "name", "sort"})
public class Publisher implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Display name
     */
    @NotNull
    @JsonProperty("name")
    private final String name;

    /**
     * Name used for sorting
     */
    @JsonProperty("sort")
    private final String sort;

    /**
     * Constructor used to define a publisher
     * @param id Unique id
     * @param name Display name
     * @param sort Name used for sorting
     */
    public Publisher(@JsonProperty("id") final Integer id,
                     @JsonProperty("name") final String name,
                     @JsonProperty("sort") final String sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
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
     * Publisher display name
     *
     * @return string containing the publisher name
     */
    public String getName() {
        return name;
    }

    /**
     * Name used for sorting
     *
     * @return String containing the publisher name used for sorting
     */
    public String getSort() {
        return sort;
    }
}
