package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;
import net.kelsier.bookshelf.api.patterns.RegexPatterns;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

/**
 * Book metadata consisting of data pulled from various tables
 */
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
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

    /**
     * Book title
     */
    @NotNull
    @JsonProperty("title")
    private final String title;

    /**
     * Book author
     */
    @NotNull
    @JsonProperty("author")
    private final String author;

    /**
     * Book series
     */
    @NotNull
    @JsonProperty("series")
    private final String series;

    /**
     * Series index if book is part of a series
     */
    @NotNull
    @Min(0)
    @JsonProperty("seriesIndex")
    private final Integer seriesIndex;

    /**
     * Name of the book publisher
     */
    @NotNull
    @JsonProperty("publisher")
    private final String publisher;

    /**
     *  ISBN Value
     *  ISBN prior to 2007 is a 10-digit number.
     *  ISBN from 2007 onwards consists of a 13-digit number is divided into five parts of variable length, each part separated by a hyphen.
     */
    @Length(min = 10, max = 17)
    @JsonProperty("isbn")
    private final String isbn;

    /**
     * Language book is written in
     */
    @NotNull
    @JsonProperty("language")
    private final String language;

    /**
     * eBook format
     */
    @NotNull
    @JsonProperty("format")
    private final String format;

    /**
     * Uncompressed size of the book
     */
    @NotNull
    @Min(0)
    @JsonProperty("size")
    private final Integer size;

    /**
     * Flag indicating if the book contains a cover
     */
    @JsonProperty("hasCover")
    private final Boolean hasCover;

    /**
     * Date that the book was added to the database
     */
    @JsonProperty("dateAdded")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp dateAdded;

    /**
     * Date that the book was published
     */
    @JsonProperty("publicationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp publicationDate;

    /**
     * Date that the book metadata was last modified
     */
    @NotNull
    @JsonProperty("lastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp lastModified;

    /**
     * Relative path to the book
     */
    @NotNull
    @JsonProperty("path")
    @Pattern(regexp = RegexPatterns.FILENAME_REGEX, message = "invalid path format")
    private final String path;

    /**
     * Book comments
     */
    @NotNull
    @JsonProperty("comments")
    private final String comments;

    /**
     * Constructor used to define book data
     * @param id Unique id
     * @param title Book title
     * @param author Book author
     * @param series Book series name
     * @param seriesIndex Series index if book is part of a series
     * @param publisher Name of the book publisher
     * @param isbn ISBN value
     * @param language Language book is written in
     * @param format eBook format
     * @param size Uncompressed size of the book
     * @param hasCover Flag indicating if the book contains a cover
     * @param dateAdded Date that the book was added to the database
     * @param publicationDate Date that the book was published
     * @param lastModified Date that the book metadata was last modified
     * @param path Relative path to the book
     * @param comments Book comments
     */
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

    /**
     * Unique id
     * @return integer containing the unique id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Book title
     * @return string containing the name of the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Book author
     * @return string containing the name of the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Book series
     * @return string containing the name of the book series
     */
    public String getSeries() {
        return series;
    }

    /**
     * Series index
     * @return integer containing the series index if book is part of a series
     */
    public Integer getSeriesIndex() {
        return seriesIndex;
    }

    /**
     * Book publisher
     * @return string containing the name of the book publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * ISBN value
     * @return string containing the ISBN value
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Book language
     * @return string containing the language that the book is written in
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Book format
     * @return string containing the name of the book format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Book size
     * @return stringi containing the uncompressed size of the book
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Book conver
     * @return true if the book has a cover
     */
    public Boolean getHasCover() {
        return hasCover;
    }

    /**
     * Date that the book was added to the database
     * @return Date that the book was added to the database
     */
    public Timestamp getDateAdded() {
        return dateAdded;
    }

    /**
     * Date that the book was published
     * @return Date that the book was published
     */
    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    /**
     * Date that the book metadata was last modified
     * @return Date that the book metadata was last modified
     */
    public Timestamp getLastModified() {
        return lastModified;
    }

    /**
     * Path to book
     * @return string containing the relative path to the book
     */
    public String getPath() {
        return path;
    }

    /**
     * Comments
     * @return string containing any book comments
     */
    public String getComments() {
        return comments;
    }
}
