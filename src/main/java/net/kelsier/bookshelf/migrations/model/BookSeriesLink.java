package net.kelsier.bookshelf.migrations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE TABLE books_series_link (
 *     id SERIAL PRIMARY KEY,
 *     book INTEGER NOT NULL,
 *     series INTEGER NOT NULL,
 *     UNIQUE(book)
 * );
 */
public class BookSeriesLink {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("series")
    @Min(1)
    private final Integer series;

    public BookSeriesLink(final Integer id, final Integer book, final Integer series) {
        this.id = id;
        this.book = book;
        this.series = series;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getSeries() {
        return series;
    }
}
