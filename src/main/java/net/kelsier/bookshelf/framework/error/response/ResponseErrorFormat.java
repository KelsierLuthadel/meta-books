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

package net.kelsier.bookshelf.framework.error.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kelsier.bookshelf.framework.error.APIResponseError;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.kelsier.bookshelf.framework.error.response.RegexPatterns.UUID_REGEX;

/**
 * Definition of the response object for APIs
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class ResponseErrorFormat {
    /**
     * an HTTP response code
     */
    @Min(200)
    private final int status;

    /**
     * a UUID used to track the exception in log files
     */
    @NotNull
    @Pattern(regexp = UUID_REGEX)
    private final String reference;

    /**
     * The message
     */
    @NotBlank
    private final String message;

    /**
     * an optional error code
     */
    private final Integer errorCode;

    /**
     * an optional help URL that describes the original calling API
     */
    @NotBlank
    private final String helpUrl;

    /**
     * an optional list of errors that relates to reason for the exception
     */
    @Valid
    private final List<APIResponseError> errors;

    /**
     * @param status    an HTTP response code
     * @param reference a UUID used to track the exception in log files
     * @param message   a message
     * @param helpUrl   an optional help URL that describes the original calling API
     * @param errors    an optional list of errors that relates to reason for the exception
     */
    @JsonCreator
    public ResponseErrorFormat(@JsonProperty("status") final int status,
                               @JsonProperty("reference") final String reference,
                               @JsonProperty("message") final String message,
                               @JsonProperty("helpUrl") final String helpUrl,
                               @JsonProperty("errors") final List<APIResponseError> errors) {
        this.status = status;
        this.reference = reference;
        this.message = message;
        this.errorCode = null;
        this.errors = Objects.requireNonNullElseGet(errors, ArrayList::new);
        this.helpUrl = helpUrl;
    }

    /**
     * @param status    an HTTP response code
     * @param reference a UUID used to track the exception in log files
     * @param message   a  message
     * @param errorCode an optional error code
     * @param helpUrl   an optional help URL that describes the original calling API
     * @param errors    an optional list of errors that relates to reason for the exception
     */
    @JsonCreator
    public ResponseErrorFormat(@JsonProperty("status") final int status,
                               @JsonProperty("reference") final String reference,
                               @JsonProperty("message") final String message,
                               @JsonProperty("error_code") final Integer errorCode,
                               @JsonProperty("helpUrl") final String helpUrl,
                               @JsonProperty("errors") final List<APIResponseError> errors) {
        this.status = status;
        this.reference = reference;
        this.message = message;
        this.errorCode = errorCode;
        this.errors = Objects.requireNonNullElseGet(errors, ArrayList::new);
        this.helpUrl = helpUrl;
    }

    /**
     * @return an HTTP response code
     */
    public int getStatus() { return status; }

    /**
     * @return a UUID used to track the exception in log files
     */
    public String getReference() { return reference; }

    /**
     * @return the message
     */
    public String getMessage() { return message; }

    /**
     * @return an optional error code
     */
    public Integer getErrorCode() { return errorCode; }

    /**
     * @return an optional help URL that describes the original calling API
     */
    public String getHelpUrl() { return helpUrl; }

    /**
     * @return an optional list of errors that relates to reason for the exception
     */
    public List<APIResponseError> getErrors() { return errors; }
}
