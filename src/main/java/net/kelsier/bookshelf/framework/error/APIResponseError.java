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

package net.kelsier.bookshelf.framework.error;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Use to return a response error
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings("unused")
public final class APIResponseError implements Serializable {
    private static final long serialVersionUID = -4876738904546026913L;

    /**
     * Details about one of potentially several errors caused by an action
     */
    @NotBlank
    private final String detail;

    /**
     * The specific element of a request or component in the backend causing this error
     */
    private final String cause;

    /**
     * an optional error code
     */
    private final Integer errorCode;

    /**
     * Create a new response error.
     * 
     * @param detail    - Details about one of potentially several errors caused by an action
     * @param cause     - The specific element of a request or component in the backend causing this error
     * @param errorCode - The error code for this error
     */
    public APIResponseError(final String detail, final String cause, final Integer errorCode) {
        this.detail = detail;
        this.cause = cause;
        this.errorCode = errorCode;
    }

    /**
     * Create a new response error.
     * 
     * @param detail    - Details about one of potentially several errors caused by an action
     * @param errorCode - The error code for this error
     */
    public APIResponseError(final String detail, final Integer errorCode) {
        this(detail, null, errorCode);
    }

    /**
     * Create a new response error.
     * 
     * @param detail - Details about one of potentially several errors caused by an action
     * @param cause  - The specific element of a request or component in the backend causing this error
     */
    public APIResponseError(final String detail, final String cause) {
        this(detail, cause, null);
    }

    /**
     * Create a new response error.
     * 
     * @param detail - Details about one of potentially several errors caused by an action
     */
    public APIResponseError(final String detail) {
        this(detail, null, null);
    }

    /**
     * Create a new response error.
     * 
     * @param apiResponseError - Takes an ResponseError to construct the new ResponseError. Used for cloning
     */
    public APIResponseError(final APIResponseError apiResponseError) {
        this(apiResponseError.detail, apiResponseError.cause, apiResponseError.errorCode);
    }

    /**
     * Retrieve the detail.
     * 
     * @return Details about one of potentially several errors caused by an action
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Retrieve the cause.
     * 
     * @return The specific element of a request or component in the backend causing this error
     */
    public String getCause() {
        return cause;
    }

    /**
     * Retrieve the error code.
     * 
     * @return The error code for this error
     */
    public Integer getErrorCode() {
        return errorCode;
    }
}
