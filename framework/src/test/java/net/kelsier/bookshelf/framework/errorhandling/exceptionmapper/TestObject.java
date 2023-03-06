package net.kelsier.bookshelf.framework.errorhandling.exceptionmapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

@SuppressWarnings("unused")
@JsonIgnoreProperties({"hello"})
public class TestObject {
    @NotBlank
    private final String string;

    public TestObject(@JsonProperty("string") final String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
