package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Tag details as represented by the authors table
 */
@JsonPropertyOrder({"id", "name"})
public class Tag implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Tag display name
     */
    @NotNull
    @JsonProperty("name")
    private final String name;

    /**
     * Constructor used to define an author
     * @param id Unique id
     * @param name Tag display name
     */
    public Tag(@NotNull @JsonProperty("id") @Min(1) final Integer id,
               @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
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
     * Tag display name
     *
     * @return string containing the tag name
     */
    public String getName() {
        return name;
    }
}
