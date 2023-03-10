package net.kelsier.bookshelf.api.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"id", "languageCode"})
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class Language implements Entity {
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

    @Override
    public int getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
