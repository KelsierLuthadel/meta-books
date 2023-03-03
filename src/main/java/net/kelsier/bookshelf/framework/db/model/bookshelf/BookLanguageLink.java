package net.kelsier.bookshelf.framework.db.model.bookshelf;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE TABLE books_languages_link (
 *     id SERIAL PRIMARY KEY,
 *     book INTEGER NOT NULL,
 *     lang_code INTEGER NOT NULL,
 *     UNIQUE(book, lang_code)
 * );
 */
public class BookLanguageLink {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("book")
    @Min(1)
    private final Integer book;

    @NotNull
    @JsonProperty("languageCode")
    @Min(1)
    private final Integer languageCode;

    public BookLanguageLink(final Integer id, final Integer book, final Integer languageCode) {
        this.id = id;
        this.book = book;
        this.languageCode = languageCode;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getLanguageCode() {
        return languageCode;
    }
}
