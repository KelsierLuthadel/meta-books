package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.patterns.RegexPatterns;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

/**
 * Book details as represented by the books table
 */
@JsonPropertyOrder({"id", "title", "sort", "seriesIndex", "isbn", "hasCover", "publicationDate", "dateAdded", "lastModified"})
public class Book implements Entity {
    /**
     * Unique id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * Display title
     */
    @NotNull
    @JsonProperty("title")
    private final String title;

    /**
     * Title used for sorting
     */
    @JsonProperty("sort")
    private final String sort;

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
     * Book number if the book is part of a series
     */
    @NotNull
    @JsonProperty("seriesIndex")
    private final Integer seriesIndex;

    /**
     *  ISBN Value
     *  ISBN prior to 2007 is a 10-digit number.
     *  ISBN from 2007 onwards consists of a 13-digit number is divided into five parts of variable length, each part separated by a hyphen.
     */
    @JsonProperty("isbn")
    private final String isbn;

    /**
     * Relative path to the book
     */
    @NotNull
    @JsonProperty("path")
    @Pattern(regexp = RegexPatterns.FILENAME_REGEX, message = "invalid path format")
    private final String path;

    /**
     * Flag to indicate if book has a cover
     */
    @JsonProperty("hasCover")
    private final Boolean hasCover;

    /**
     * Date that the book was last modified
     */
    @NotNull
    @JsonProperty("lastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp lastModified;

    /**
     * Constructor used to define a book
     * @param id Unique id
     * @param title Display title
     * @param sort Title used for sorting
     * @param dateAdded Date that the book was added to the database
     * @param publicationDate Date that the book was published
     * @param seriesIndex Book number if the book is part of a series
     * @param isbn Book ISBN code
     * @param path  Relative path to the book
     * @param hasCover Flag to indicate if book has a cover
     * @param lastModified Date that the book was last modified
     */
    public Book(@JsonProperty("id") final Integer id,
                @JsonProperty("title") final String title,
                @JsonProperty("sort") final String sort,
                @JsonProperty("dateAdded") final Timestamp dateAdded,
                @JsonProperty("publicationDate") final Timestamp publicationDate,
                @JsonProperty("seriesIndex") final Integer seriesIndex,
                @JsonProperty("isbn") final String isbn,
                @JsonProperty("path") final String path,
                @JsonProperty("hasCover") final Boolean hasCover,
                @JsonProperty("lastModified") final Timestamp lastModified) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.dateAdded = dateAdded;
        this.publicationDate =  publicationDate;
        this.seriesIndex = seriesIndex;
        this.isbn = isbn;
        this.path = path;
        this.hasCover = hasCover;
        this.lastModified = lastModified;
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
     * Display title
     *
     * @return String containing the book title
     */
    public String getTitle() {
        return title;
    }


    /**
     * Title used for sorting
     *
     * @return String containing the book title used for sorting
     */
    public String getSort() {
        return sort;
    }

    /**
     * Date that the book was added to the database
     *
     * @return A Timestamp containing the date that the book was added to the database
     */
    public Timestamp getDateAdded() {
        return dateAdded;
    }

    /**
     * Date that the book was published
     *
     * @return A Timestamp containing the date that the book was published
     */
    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    /**
     * Book number if the book is part of a series
     *
     * @return Integer containing the book number if the book is part of a series
     */
    public Integer getSeriesIndex() {
        return seriesIndex;
    }

    /**
     * Book ISBN code
     *
     * @return String containing the book ISBN code
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Relative path to book
     *
     * @return String containing the relative path to the book
     */
    public String getPath() {
        return path;
    }

    /**
     * Flag to indicate if book has a cover
     *
     * @return Boolean indicating if book has a cover
     */
    public Boolean getHasCover() {
        return hasCover;
    }

    /**
     * Date that the book was last modified
     *
     * @return A Timestamp representing the date that the book was last modified
     */
    public Timestamp getLastModified() {
        return lastModified;
    }
}
