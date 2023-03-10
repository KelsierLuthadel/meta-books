/*
 * Copyright (c) Kelsier Luthadel 2023.
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
 *
 */

package net.kelsier.bookshelf.api.db.model.customcolumn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.kelsier.bookshelf.api.db.model.Entity;
import net.kelsier.bookshelf.api.filter.EmptyValueFilter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"id", "label", "name", "dataType", "display", "multiple", "normalized"})
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = EmptyValueFilter.class)
public class CustomColumns implements Entity {
    @JsonProperty("id")
    @NotNull
    @Min(1)
    private final Integer id;

    @JsonProperty("label")
    @NotNull
    private final String label;

    @JsonProperty("name")
    @NotNull
    private final String name;

    @JsonProperty("dataType")
    @NotNull
    private final String dataType;

    @JsonProperty("display")
    @NotNull
    private final String display;

    @JsonProperty("multiple")
    @NotNull
    private final Boolean multiple;

    @JsonProperty("normalized")
    @NotNull
    private final Boolean normalized;

    public CustomColumns( @JsonProperty("id") @NotNull @Min(1) final Integer id,
                          @JsonProperty("label") @NotNull final String label,
                          @JsonProperty("name") @NotNull final String name,
                          @JsonProperty("dataType") @NotNull final String dataType,
                          @JsonProperty("display") @NotNull final String display,
                          @JsonProperty("multiple") @NotNull final Boolean multiple,
                          @JsonProperty("normalized") @NotNull final Boolean normalized) {
        this.id = id;
        this.label = label;
        this.name = name;
        this.dataType = dataType;
        this.display = display;
        this.multiple = multiple;
        this.normalized = normalized;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDisplay() {
        return display;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public Boolean getNormalized() {
        return normalized;
    }
}
