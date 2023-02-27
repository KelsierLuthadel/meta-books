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

package net.kelsier.bookshelf.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User details
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings("unused")
@Schema(description = "User Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "username", "firstName", "lastName", "email", "roles", "enabled"})
public class UserModel {
    /**
     * User id
     */
    @NotNull
    @JsonProperty("id")
    private final Integer id;

    /**
     * User's username
     */
    @NotNull
    @JsonProperty("username")
    private final String username;

    /**
     * User's first name
     */
    @NotNull
    @JsonProperty("firstName")
    private final String firstName;

    /**
     * User's last name
     */
    @NotNull
    @JsonProperty("lastName")
    private final String lastName;

    /**
     * User's email
     */
    @NotNull
    @JsonProperty("email")
    private final String email;

    /**
     * Enabled
     */
    @NotNull
    @JsonProperty("enabled")
    private final Boolean enabled;

    /**
     * roles
     */
    @NotNull
    @JsonProperty("roles")
    private final List<Integer> roles;


    /**
     * Constructor
     *
     * @param id - User id associated with the user in the Users table
     * @param username - Unique username
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param email - Users associated email address
     * @param enabled - Enabled flag
     * @param roles - List of associated security roles
     */
    public UserModel(@JsonProperty("id") final int id,
                     @JsonProperty("username") final String username,
                     @JsonProperty("firstName") final String firstName,
                     @JsonProperty("lastName") final String lastName,
                     @JsonProperty("email") final String email,
                     @JsonProperty("enabled") final Boolean enabled,
                     @JsonProperty("roles") final List<Integer> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
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

    /**
     *
     * @return A string containing the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return A string containing the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return A string containing the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return A flag determining if the user's account is enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @return A list of security roles associated with the user
     */
    public List<Integer> getRoles() {
        return roles;
    }
}
