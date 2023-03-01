package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
id  SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    format TEXT NOT NULL ,
    uncompressed_size INTEGER NOT NULL,
    name TEXT NOT NULL,
 */
public class Data {
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
    @JsonProperty("text")
    private final String text;

    public Data(final Integer id, final Integer book, final String format, final Integer uncompressedSize, final String text) {
        this.id = id;
        this.book = book;
        this.format = format;
        this.uncompressedSize = uncompressedSize;
        this.text = text;
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

    public String getText() {
        return text;
    }
}
