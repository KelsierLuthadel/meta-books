package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Author details as represented by the authors table
 */
@JsonPropertyOrder({"id", "name", "sort"})
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Author implements Entity {

    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Author display name
     */
    @JsonProperty("name")
    @NotNull
    private final String name;

    /**
     * Author name used for sorting
     */
    @JsonProperty("sort")
    private final String sort;

    /**
     * Constructor used to define an author
     * @param id Unique id
     * @param name Author display name
     * @param sort Author name used for sorting
     */
    public Author(
            @JsonProperty("id") @NotNull @Min(1) final Integer id,
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
     * Author display name
     *
     * @return string containing the author's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Author name used for sorting
     *
     * @return string containing the author's name used for sorting
     */
    public String getSort() {
        return sort;
    }
}
