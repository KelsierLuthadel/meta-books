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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User details for returning data through REST with sensistive information removed
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Schema(description = "User Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "username", "firstName", "lastName", "email", "roles", "enabled"})
public class UserModel extends  BaseUser {
    /**
     * User id
     */
    @NotNull
    @JsonProperty("id")
    private final Integer id;

    /**
     * Constructor
     *
     * @param id User id associated with the user in the Users table
     * @param username Unique username
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email Users associated email address
     * @param enabled Enabled flag
     * @param roles List of associated security roles
     */
    public UserModel(@JsonProperty("id") final int id,
                     @JsonProperty("username") final String username,
                     @JsonProperty("firstName") final String firstName,
                     @JsonProperty("lastName") final String lastName,
                     @JsonProperty("email") final String email,
                     @JsonProperty("enabled") final Boolean enabled,
                     @JsonProperty("roles") final List<Integer> roles) {
        super(username, firstName, lastName, email, enabled, roles);
        this.id = id;
    }

    /**
     *
     * @return User id
     */
    public Integer getId() {
        return id;
    }
}
