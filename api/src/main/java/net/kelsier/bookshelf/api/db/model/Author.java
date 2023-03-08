package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Author details as represented by the authors table
 */
@JsonPropertyOrder({"id", "name", "sort"})
public class Author implements Entity {

    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Author display name
     */
    @NotNull
    @JsonProperty("name")
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
            @NotNull @JsonProperty("id") @Min(1) final Integer id,
            @NotNull @JsonProperty("name") final String name,
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
