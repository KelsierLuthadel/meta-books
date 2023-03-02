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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
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

/**
 * Role details DAO
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings("unused")
@Schema(description = "Role Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "role", "description"})
public class UserRole {
    /**
     * Role id
     */
    @NotNull
    @JsonProperty("id")
    private final Integer id;

    /**
     * role name
     */
    @NotNull
    @JsonProperty("role")
    private final String role;

    /**
     * role description
     */
    @NotNull
    @JsonProperty("description")
    private final String description;


    /**
     * Constructor
     *
     * @param id Role id
     * @param role Role name
     * @param description role description
     */
    public UserRole(@JsonProperty("id") final int id,
                    @JsonProperty("role") final String role,
                    @JsonProperty("description") final String description) {
        this.id = id;
        this.role = role;
        this.description = description;
    }

    /**
     *
     * @return Role id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @return A string containing the role id
     */
    public String getRole() {
        return role;
    }

    /**
     *
     * @return A string containing   a description
     */
    public String getDescription() {
        return description;
    }
}
