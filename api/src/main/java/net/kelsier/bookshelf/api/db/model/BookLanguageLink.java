package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * books_languages_link (id SERIAL PRIMARY KEY, book INTEGER NOT NULL, lang_code INTEGER NOT NULL, UNIQUE(book, lang_code))
 */

public class BookLanguageLink implements Entity {
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

    @Override
    public int getId() {
        return id;
    }

    public Integer getBook() {
        return book;
    }

    public Integer getLanguageCode() {
        return languageCode;
    }
}
