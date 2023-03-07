/*
 * Copyright (c) 2023 Kelsier Luthadel
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
 */

package net.kelsier.bookshelf.framework.db.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.framework.patterns.RegexPatterns;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * User details
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Schema(description = "User Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "username", "firstname", "lastname", "email", "enabled", "password", "roles"})
public class DatabaseUser {
    /**
     * User id
     */
    @NotNull
    @JsonProperty("id")
    @Min(1)
    private final Integer id;

    /**
     * User's username
     */
    @NotBlank
    @JsonProperty("username")
    @Size(max = 20)
    private final String username;

    /**
     * User's first name
     */
    @JsonProperty("firstName")
    @Size(max = 20)
    private final String firstName;

    /**
     * User's last name
     */
    @JsonProperty("lastName")
    @Size(max = 20)
    private final String lastName;

    /**
     * User's email
     */
    @JsonProperty("email")
    @Pattern(regexp = RegexPatterns.EMAIL_REGEX, message = "invalid format")
    private final String email;

    /**
     * Enabled
     */
    @NotNull
    @JsonProperty("enabled")
    private final Boolean enabled;


    /**
     * password
     */
    @NotNull
    @JsonProperty("password")
    @Pattern(regexp = RegexPatterns.PASSWORD_REGEX, message = "does not meet minimum requirements")
    private final String password;

    /**
     * roles
     */
    @JsonProperty("roles")
    private final List<Integer> roles;


    /**
     * Constructor
     *
     * @param id User id
     * @param password User's password
     */
    public DatabaseUser(@JsonProperty("id") final Integer id,
                        @JsonProperty("username") final String username,
                        @JsonProperty("firstName") final String firstName,
                        @JsonProperty("lastName") final String lastName,
                        @JsonProperty("email") final String email,
                        @JsonProperty("enabled") final Boolean enabled,
                        @JsonProperty("password") final String password,
                        @JsonProperty("roles") final List<Integer> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
        this.password = password;
        this.roles = roles;
    }

    /**
     *
     * @return User id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @return A string containing the user's name
     */
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     *
     * @return A string containing the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return The users role id
     */
    public List<Integer> getRoles() {
        return roles;
    }
}
