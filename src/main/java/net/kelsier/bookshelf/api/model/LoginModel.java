package net.kelsier.bookshelf.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class LoginModel {
    @NotNull
    @JsonProperty("username")
    final String username;

    @NotNull
    @JsonProperty("password")
    final String password;

    public LoginModel(@JsonProperty("username") final String username, @JsonProperty("password") final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
