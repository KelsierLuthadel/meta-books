package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Book data details as represented by the books table
 */
@JsonPropertyOrder({"id", "book", "format", "uncompressedSize", "name"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookData implements Entity {
    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Book id used to find a book in the books table
     */
    @JsonProperty("book")
    @NotNull
    @Min(1)
    private final Integer book;


    /**
     * Book format
     */
    @JsonProperty("format")
    @NotNull
    private final String format;


    /**
     * Uncompressed size of book
     */
    @JsonProperty("uncompressedSize")
    @NotNull
    private final Integer uncompressedSize;


    /**
     * Display name
     */
    @JsonProperty("name")
    @NotNull
    private final String name;

    /**
     * Constructor used to define book data
     *
     * @param id Unique id
     * @param book Book id
     * @param format Book format
     * @param uncompressedSize Uncompressed size of book
     * @param name Display name
     */
    public BookData(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                    @JsonProperty("book") @NotNull @Min(1) final Integer book,
                    @JsonProperty("format") @NotNull final String format,
                    @JsonProperty("uncompressedSize") @NotNull final Integer uncompressedSize,
                    @JsonProperty("name") @NotNull final String name) {
        this.id = id;
        this.book = book;
        this.format = format;
        this.uncompressedSize = uncompressedSize;
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
     * Book id used to find a book in the books table
     *
     * @return int containing the book id
     */
    public Integer getBook() {
        return book;
    }

    /**
     * Book format
     *
     * @return string containing the book format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Uncompressed size of book
     *
     * @return int containing the uncompressed size of the book
     */
    public Integer getUncompressedSize() {
        return uncompressedSize;
    }

    /**
     * Display name
     *
     * @return string containing the book display name
     */
    public String getName() {
        return name;
    }
}
