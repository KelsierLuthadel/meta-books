package net.kelsier.bookshelf.framework.db.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class LoginModel {
    @NotNull
    @JsonProperty("username")
    private final String username;

    @NotNull
    @JsonProperty("password")
    private final String password;

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
