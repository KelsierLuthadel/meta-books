package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * languages(id SERIAL PRIMARY KEY, lang_code TEXT NOT NULL, UNIQUE(lang_code))
 */

public class Language {
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    @NotNull
    @JsonProperty("languageCode")
    private final String languageCode;

    public Language(@JsonProperty("id") final Integer id, @JsonProperty("languageCode") final String languageCode) {
        this.id = id;
        this.languageCode = languageCode;
    }

    public Integer getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
