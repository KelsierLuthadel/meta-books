package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * Series details as represented by the series table
 */
@JsonPropertyOrder({"id", "name", "sort"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Series implements Entity {
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
     * Constructor used to define a series
     * @param id  Unique id
     * @param name Display name
     * @param sort Name used for sorting
     */
    public Series(@JsonProperty("id") @NotNull @Min(1) final Integer id,
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
     * Display name
     *
     * @return String containing the series name
     */
    public String getName() {
        return name;
    }

    /**
     * Name used for sorting
     *
     * @return String containing the series name used for sorting
     */
    public String getSort() {
        return sort;
    }
}
