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

package net.kelsier.bookshelf.framework.error.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import net.kelsier.bookshelf.framework.error.APIResponseError;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Indicates an error with the response
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
@JsonIgnoreProperties({ "cause", "stackTrace", "errorCode", "suppressed", "localizedMessage"})
@XmlRootElement(name = "responseErrorException")
@Schema(name = "responseErrorException", title = "Response exception", description = "Response error exception")
public final class ResponseException extends Exception {
    private static final long serialVersionUID = -5298556848196356566L;
    /**
     * HTTP response code
     */
    @Min(200)
    private final int status;

    /**
     * Reason for the error
     */
    @NotBlank
    private final String message;

    /**
     * An optional error code
     */
    private final Integer errorCode;

    /**
     * URL which can provide additional support or information
     */
    private final String helpUrl;

    /**
     * A list of errors that resulted from an action. This optional array provides more information than the message
     */
    @Valid
    private final List<APIResponseError> errors;

    /**
     * Constructor
     * 
     * @param status  - HTTP response code
     * @param message - The reason for the error
     * @param helpUrl - A url which can provide additional support or information
     * @param errors  - A list of errors that resulted from an action. This optional array provides more information than the message
     */
    @JsonCreator
    public ResponseException(@JsonProperty("status") final int status,
                             @JsonProperty("message") final String message,
                             @JsonProperty("helpUrl") final String helpUrl,
                             @JsonProperty("errors") final List<APIResponseError> errors) {
        this(status, message, null, helpUrl, errors);
    }

    /**
     * Constructor
     *
     * @param status  - HTTP response code
     * @param message - The reason for the error
     */
    @JsonCreator
    public ResponseException(@JsonProperty("status") final int status,
                             @JsonProperty("message") final String message) {
        this(status, message, null, null, null);
    }

    /**
     * Constructor
     *
     * @param status    - HTTP response code
     * @param message   - The reason for the error
     * @param errorCode - An optional error code
     * @param helpUrl   - A url which can provide additional support or information
     * @param errors    - A list of errors that resulted from an action. This optional array provides more information than the message
     */
    @JsonCreator
    public ResponseException(@JsonProperty("status") final int status,
                             @JsonProperty("message") final String message,
                             @JsonProperty("error_code") final Integer errorCode,
                             @JsonProperty("helpUrl") final String helpUrl,
                             @JsonProperty("errors") final List<APIResponseError> errors) {
        super(message);
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.helpUrl = helpUrl;
        if (errors == null) {
            this.errors = new ArrayList<>();
        } else {
            this.errors = copyErrors(errors);
        }
    }

    /**
     * Creates a deep copy of a list of ResponseErrors.
     * 
     * @param errors - The list of errors to copy
     * @return - A deep copy of the passed list of ResponseErrors
     */
    private static List<APIResponseError> copyErrors(final List<APIResponseError> errors) {
        return errors.stream().map(APIResponseError::new).collect(Collectors.toList());
    }

    /**
     * Retrieves the status code.
     * 
     * @return HTTP response code
     */
    public int getStatus() { return status; }

    /**
     * Retrieves the message.
     * 
     * @return Reason for the error
     */
    @Override
    public String getMessage() { return message; }

    /**
     * Retrieves the error code.
     * 
     * @return An optional error code
     */
    public Integer getErrorCode() { return errorCode; }

    /**
     * Retrieves the help url.
     * 
     * @return URL which can provide additional support or information
     */
    public String getHelpUrl() { return helpUrl; }

    /**
     * Retrieves the errors.
     * 
     * @return A list of errors that resulted from an action. This optional array provides more information than the message
     */
    public List<APIResponseError> getErrors() {
        return copyErrors(errors);
    }
}
