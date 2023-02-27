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

package net.kelsier.bookshelf.framework.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static net.kelsier.bookshelf.framework.error.response.RegexPatterns.*;

/**
 * User details
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Schema(description = "User Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "username", "firstname", "lastname", "email", "enabled", "password", "roles"})
public class User {
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
    @NotNull
    @NotBlank
    @JsonProperty("username")
    @Size(max = 20)
    private String username;

    /**
     * User's first name
     */
    @JsonProperty("firstName")
    @Size(max = 20)
    private String firstName;

    /**
     * User's last name
     */
    @JsonProperty("lastName")
    @Size(max = 20)
    private String lastName;

    /**
     * User's email
     */
    @JsonProperty("email")
    @Pattern(regexp = EMAIL_REGEX, message = "Invalid email format")
    private String email;

    /**
     * Enabled
     */
    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;


    /**
     * password
     */
    @NotNull
    @JsonProperty("password")
    @Size(max = 30)
    @Pattern(regexp = PASSWORD_REGEX)
    private String password;

    /**
     * roles
     */
    @NotNull
    @JsonProperty("roles")
    private List<Integer> roles;


    /**
     * Constructor
     *
     * @param id User id
     * @param password User's password
     */
    public User(@JsonProperty("id") final int id,
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

    public Boolean getEnabled() {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
}
