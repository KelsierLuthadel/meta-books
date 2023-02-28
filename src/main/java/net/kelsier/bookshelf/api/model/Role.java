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
import javax.validation.constraints.Size;

/**
 * Role details
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@Schema(description = "Role Details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"role", "description"})
public class Role {
    /**
     * Role
     */
    @NotNull
    @Size(min = 3, max = 30)
    @JsonProperty("role")
    private final String roleName;

    /**
     * Role description
     */
    @Size(min = 3,max = 30)
    @JsonProperty("description")
    private final String description;


    /**
     * Constructor
     *
     * @param roleName - Unique role
     * @param description - role description
     */
    public Role(@JsonProperty("role") final String roleName,
                @JsonProperty("description") final String description) {
        this.roleName = roleName;
        this.description = description;
    }

    /**
     *
     * @return A string containing the role
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     *
     * @return A string containing the role description
     */
    public String getDescription() {
        return description;
    }
}
