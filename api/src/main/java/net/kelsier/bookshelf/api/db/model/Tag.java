package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Tag details as represented by the authors table
 */
@JsonPropertyOrder({"id", "name"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Tag implements Entity {
    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Tag display name
     */
    @JsonProperty("name")
    @NotNull
    private final String name;

    /**
     * Constructor used to define an author
     * @param id Unique id
     * @param name Tag display name
     */
    public Tag(@JsonProperty("id") @NotNull @Min(1)  final Integer id,
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
