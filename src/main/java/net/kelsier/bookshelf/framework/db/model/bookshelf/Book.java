package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/*
    Books table

    public.books (
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
    )
 */
@JsonPropertyOrder({"id", "title", "sort", "seriesIndex", "isbn", "hasCover", "publicationDate", "dateAdded", "lastModified"})
public class Book {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("title")
    private final String title;

    @JsonProperty("sort")
    private final String sort;

    @JsonProperty("dateAdded")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp dateAdded;

    @JsonProperty("publicationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp publicationDate;

    @NotNull
    @JsonProperty("seriesIndex")
    private final Double seriesIndex;

    @JsonProperty("isbn")
    private final String isbn;

    @NotNull
    @JsonProperty("path")
    @JsonIgnore
    private final String path;

    @JsonProperty("hasCover")
    private final Boolean hasCover;

    @NotNull
    @JsonProperty("lastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
