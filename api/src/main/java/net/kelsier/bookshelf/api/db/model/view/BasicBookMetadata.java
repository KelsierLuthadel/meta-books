/*
 * Copyright (c) Kelsier Luthadel 2023.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package net.kelsier.bookshelf.api.db.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.db.model.Entity;
import net.kelsier.bookshelf.api.patterns.RegexPatterns;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Book metadata consisting of data pulled from various tables
 */
@JsonPropertyOrder({"id", "title", "authorId", "author", "series", "seriesIndex", "publisher", "isbn", "identifier",
                    "language", "format","size", "hasCover", "dateAdded", "publicationDate", "lastModified", "path", "comments"})
@JsonInclude(NON_EMPTY)
public class BasicBookMetadata implements Entity {
    /**
     * Unique id
     */
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    /**
     * Book title
     */
    @JsonProperty("title")
    @NotNull
    private final String title;

    /**
     * Book author ID
     */
    @JsonProperty("authorId")
    @NotNull
    private final Integer authorID;

    /**
     * Book author
     */
    @JsonProperty("author")
    @NotNull
    private final String author;

    /**
     * Book series
     */
    @JsonProperty("series")
    private final String series;

    /**
     * Series index if book is part of a series
     */
    @JsonProperty("seriesIndex")
    @Min(0)
    private final Integer seriesIndex;

    /**
     * Name of the book publisher
     */
    @JsonProperty("publisher")
    private final String publisher;

    /**
     *  ISBN Value
     *  ISBN prior to 2007 is a 10-digit number.
     *  ISBN from 2007 onwards consists of a 13-digit number is divided into five parts of variable length, each part separated by a hyphen.
     */
    @JsonProperty("isbn")
    private final String isbn;

    /**
     * Language book is written in
     */
    @JsonProperty("language")
    private final String language;

    /**
     * eBook format
     */
    @JsonProperty("format")
    private final String format;

    /**
     * Uncompressed size of the book
     */
    @JsonProperty("size")
    @Min(0)
    private final Integer size;

    /**
     * Flag indicating if the book contains a cover
     */
    @JsonProperty("hasCover")
    private final Boolean hasCover;

    /**
     * Date that the book was published
     */
    @JsonProperty("publicationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Timestamp publicationDate;

    /**
     * Relative path to the book
     */
    @JsonProperty("path")
    @NotNull
    @Pattern(regexp = RegexPatterns.FILENAME_REGEX, message = "invalid path format")
    private final String path;


    @JsonProperty("tags")
    private final Tags tags;

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
     * @param publicationDate Date that the book was published
     * @param path Relative path to the book
     * @param tags List of tags
     */
    public BasicBookMetadata(@JsonProperty("id") @NotNull @Min(1) final Integer id,
                             @JsonProperty("title") @NotNull final String title,
                             @JsonProperty("authorId") @NotNull final Integer authorID,
                             @JsonProperty("author") @NotNull final String author,
                             @JsonProperty("series") final String series,
                             @JsonProperty("seriesIndex") @Min(0) final Integer seriesIndex,
                             @JsonProperty("publisher") final String publisher,
                             @JsonProperty("isbn")final String isbn,
                             @JsonProperty("language") final String language,
                             @JsonProperty("format") final String format,
                             @JsonProperty("size") @NotNull final Integer size,
                             @JsonProperty("hasCover") @NotNull final Boolean hasCover,
                             @JsonProperty("publicationDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") final Timestamp publicationDate,
                             @JsonProperty("path") @NotNull final String path,
                             @JsonProperty("tags") final Tags tags) {
        this.id = id;
        this.title = title;
        this.authorID = authorID;
        this.author = author;
        this.series = series;
        this.seriesIndex = seriesIndex;
        this.publisher = publisher;
        this.isbn = isbn;
        this.language = language;
        this.format = format;
        this.size = size;
        this.hasCover = hasCover;
        this.publicationDate = publicationDate;
        this.path = path;
        this.tags = tags;
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
     * Book author ID
     * @return int containing the id of the book author
     */
    public int getAuthorId() {
        return authorID;
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
     * Date that the book was published
     * @return Date that the book was published
     */
    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    /**
     * Path to book
     * @return string containing the relative path to the book
     */
    public String getPath() {
        return path;
    }

    /**
     * Tags
     * @return List containing tags
     */
    public Tags getTags() {
        return tags;
    }
}
