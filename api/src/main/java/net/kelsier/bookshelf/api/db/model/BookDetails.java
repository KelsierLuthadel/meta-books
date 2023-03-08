package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Tag details as represented by the authors table
 */
@JsonPropertyOrder({"id", "title", "author", "series", "seriesIndex", "publisher", "isbn", "language", "format",
                    "size", "hasCover", "dateAdded", "publicationDate", "lastModified", "path", "comments"})
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

    /**
     *  ISBN prior to 2007 is a 10-digit number.
     *  ISBN from 2007 onwards consists of a 13-digit number is divided into five parts of variable length, each part separated by a hyphen.
     */

    @Length(min = 10, max = 17)
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

    @JsonProperty("hasCover")
    private final Boolean hasCover;

    @JsonProperty("dateAdded")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp dateAdded;

    @JsonProperty("publicationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp publicationDate;

    @NotNull
    @JsonProperty("lastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp lastModified;

    @NotNull
    @JsonProperty("path")
    //todo sanitize
    private final String path;

    @NotNull
    @JsonProperty("comments")
    private final String comments;

    public BookDetails(@JsonProperty("id") final Integer id,
                       @JsonProperty("title")final String title,
                       @JsonProperty("author")final String author,
                       @JsonProperty("series")final String series,
                       @JsonProperty("seriesIndex")final Integer seriesIndex,
                       @JsonProperty("publisher")final String publisher,
                       @JsonProperty("isbn")final String isbn,
                       @JsonProperty("language")final String language,
                       @JsonProperty("format")final String format,
                       @JsonProperty("size")final Integer size,
                       @JsonProperty("hasCover")final Boolean hasCover,
                       @JsonProperty("dateAdded")final Timestamp dateAdded,
                       @JsonProperty("publicationDate")final Timestamp publicationDate,
                       @JsonProperty("lastModified")final Timestamp lastModified,
                       @JsonProperty("path")final String path,
                       @JsonProperty("comments")final String comments) {
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
        this.hasCover = hasCover;
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

    public Boolean getHasCover() {
        return hasCover;
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
