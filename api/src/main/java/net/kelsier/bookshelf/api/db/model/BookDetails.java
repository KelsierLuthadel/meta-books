package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Tag details as represented by the authors table
 */
public class BookDetails implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("title")
    private final String title;

    @NotNull
    @JsonProperty("author")
    private final String author;

    @NotNull
    @JsonProperty("series")
    private final String series;

    @NotNull
    @Min(0)
    @JsonProperty("seriesIndex")
    private final Integer seriesIndex;

    @NotNull
    @JsonProperty("publisher")
    private final String publisher;

    @NotNull
    @JsonProperty("isbn")
    private final String isbn;

    @NotNull
    @JsonProperty("language")
    private final String language;

    @NotNull
    @JsonProperty("format")
    private final String format;

    @NotNull
    @Min(0)
    @JsonProperty("size")
    private final Integer size;

    @NotNull
    @JsonProperty("hasCover")
    private final Boolean hascover;

    @NotNull
    @JsonProperty("dateAdded")
    private final Timestamp dateAdded;

    @NotNull
    @JsonProperty("published")
    private final Timestamp publicationDate;

    @NotNull
    @JsonProperty("lastModified")
    private final Timestamp lastModified;

    @NotNull
    @JsonProperty("path")
    private final String path;

    @JsonProperty("comments")
    private final String comments;

    public BookDetails(final Integer id, final String title, final String author, final String series, final Integer seriesIndex,
                       final String publisher, final String isbn, final String language, final String format, final Integer size,
                       final Boolean hascover, final Timestamp dateAdded, final Timestamp publicationDate, final Timestamp lastModified,
                       final String path, final String comments) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.series = series;
        this.seriesIndex = seriesIndex;
        this.publisher = publisher;
        this.isbn = isbn;
        this.language = language;
        this.format = format;
        this.size = size;
        this.hascover = hascover;
        this.dateAdded = dateAdded;
        this.publicationDate = publicationDate;
        this.lastModified = lastModified;
        this.path = path;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSeries() {
        return series;
    }

    public Integer getSeriesIndex() {
        return seriesIndex;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getLanguage() {
        return language;
    }

    public String getFormat() {
        return format;
    }

    public Integer getSize() {
        return size;
    }

    public Boolean getHascover() {
        return hascover;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public String getPath() {
        return path;
    }

    public String getComments() {
        return comments;
    }
}
