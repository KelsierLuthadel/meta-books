package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 data(id SERIAL PRIMARY KEY, book INTEGER NOT NULL, format TEXT NOT NULL, uncompressed_size INTEGER NOT NULL,
      name TEXT NOT NULL, UNIQUE(book, format))
*/

public class BookData {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("format")
    private final String format;

    @NotNull
    @JsonProperty("uncompressedSize")
    private final Integer uncompressedSize;

    @NotNull
    @JsonProperty("name")
    private final String name;

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

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public String getFormat() {
        return format;
    }

    public Integer getUncompressedSize() {
        return uncompressedSize;
    }

    public String getName() {
        return name;
    }
}
