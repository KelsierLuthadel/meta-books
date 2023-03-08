package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 data(id SERIAL PRIMARY KEY, book INTEGER NOT NULL, format TEXT NOT NULL, uncompressed_size INTEGER NOT NULL,
      name TEXT NOT NULL, UNIQUE(book, format))
*/

/**
 * Book data details as represented by the books table
 */
public class BookData implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Book id used to find a book in the books table
     */
    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;


    /**
     * Book format
     */
    @NotNull
    @JsonProperty("format")
    private final String format;


    /**
     * Uncompressed size of book
     */
    @NotNull
    @JsonProperty("uncompressedSize")
    private final Integer uncompressedSize;


    /**
     * Display name
     */
    @NotNull
    @JsonProperty("name")
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
    public BookData(@JsonProperty("id") final Integer id,
                    @JsonProperty("book") final Integer book,
                    @JsonProperty("format") final String format,
                    @JsonProperty("uncompressedSize") final Integer uncompressedSize,
                    @JsonProperty("name") final String name) {
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
    public Integer getId() {
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
     * Display nmae
     *
     * @return string containing the book display name
     */
    public String getName() {
        return name;
    }
}
