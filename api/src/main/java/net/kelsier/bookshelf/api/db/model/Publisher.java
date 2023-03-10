package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Publisher details as represented by the publishers table
 */
@JsonPropertyOrder({"id", "name", "sort"})
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Publisher implements Entity {
    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Display name
     */
    @JsonProperty("name")
    @NotNull
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
    public Publisher(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                     @JsonProperty("name") @NotNull final String name,
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
