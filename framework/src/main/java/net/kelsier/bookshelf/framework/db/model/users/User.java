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

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * User model for creating a new user
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Schema(description = "User Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"username", "firstName", "lastName", "email", "password", "roles", "enabled"})
public class User extends BaseUser {
    @JsonProperty("password")
    @Pattern(regexp = RegexPatterns.PASSWORD_REGEX, message = "does not meet minimum requirements")
    private final String password;

    /**
     * Constructor
     *
     * @param username  Unique username
     * @param firstName User's first name
     * @param lastName  User's last name
     * @param email     Users associated email address
     * @param enabled   Enabled flag
     * @param password  User's password
     * @param roles     List of associated security roles
     */
    public User(@JsonProperty("username") final String username,
                @JsonProperty("firstName") final String firstName,
                @JsonProperty("lastName") final String lastName,
                @JsonProperty("email") final String email,
                @JsonProperty("enabled") final Boolean enabled,
                @JsonProperty("password") final String password,
                @JsonProperty("roles") final List<Integer> roles) {
        super(username, firstName, lastName, email, enabled, roles);
        this.password = password;
    }

    /**
     * @return A string containing the user's password
     */
    public String getPassword() {
        return password;
    }
}
