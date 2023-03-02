package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/*
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL DEFAULT 'Unknown',
    sort TEXT ,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    publication_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    series_index REAL NOT NULL DEFAULT 1.0,
    isbn TEXT DEFAULT '' ,
    path TEXT NOT NULL DEFAULT '',
    has_cover BOOL DEFAULT false,
    last_modified TIMESTAMP NOT NULL DEFAULT '2000-01-01 00:00:00+00:00'
);
 */
public class Book {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("title")
    private final String title;

    @NotNull
    @JsonProperty("sort")
    private final String sort;

    @NotNull
    @JsonProperty("dateAdded")
    private final Timestamp dateAdded;

    @JsonProperty("publicationDate")
    private final Timestamp publicationDate;

    @JsonProperty("seriesIndex")
    private final Double seriesIndex;

    @JsonProperty("isbn")
    private final String isbn;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("hasCover")
    private final Boolean hasCover;

    @JsonProperty("lastModified")
    private final Timestamp lastModified;

    public Book(final Integer id,
                final String title,
                final String sort,
                final Timestamp dateAdded,
                final Timestamp publicationDate,
                final Double seriesIndex,
                final String isbn,
                final String path,
                final Boolean hasCover,
                final Timestamp lastModified) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.dateAdded = dateAdded;
        this.publicationDate = publicationDate;
        this.seriesIndex = seriesIndex;
        this.isbn = isbn;
        this.path = path;
        this.hasCover = hasCover;
        this.lastModified = lastModified;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSort() {
        return sort;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public Double getSeriesIndex() {
        return seriesIndex;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPath() {
        return path;
    }

    public Boolean getHasCover() {
        return hasCover;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }
}