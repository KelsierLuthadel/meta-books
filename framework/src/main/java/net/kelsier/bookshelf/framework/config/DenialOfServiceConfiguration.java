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

package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Configuration for Denial of Service filter, which is a wrapper for {@link org.eclipse.jetty.servlets}
 * <p>
 * The filter is useful for limiting resources from abuse or malicious attacks such as flooding.
 * <p>
 * The following config parameters control the behavior of the filter:
 * <dl>
 * <dt>enableFilter</dt>
 * <dd>This allows enabling or disabling of the filters
 * The default value is {@code false}
 * </dd>
 * <dt>maxRequestsPerSec</dt>
 * <dd>The maximum number of requests from a connection per
 * second. After which requests are delayed, then throttled.
 * The default values is {@code 25}
 * </dd>
 * <dt>maxRequestMs</dt>
 * <dd>How long to allow the request to live.
 * The default value is {@code 30000}
 * </dd>
 * <dt>maxWaitMs</dt>
 * <dd>How long to blocking wait for the throttle semaphore.
 * The default value is {@code 50}
 * </dd>
 * <dt>throttledRequests</dt>
 * <dd>The number of requests over the rate limit able to be
 * considered at once.
 * The default value is {@code 5}
 * </dd>
 * <dt>throttledRequests</dt>
 * <dd>The number of requests over the rate limit able to be
 * considered at once.
 * The default value is {@code 100}
 * </dd>
 * <dt>ipWhitelist</dt>
 * <dd>A comma-separated list of IP addresses that will not be rate limited
 * The default value is {@code 127.0.0.1}
 * </dd>
 * <dt>statusCode</dt>
 * <dd>The status code to send if there are too many requests.
 * The default value is {@code 429}(too many requests), but {@code 503} (Unavailable) is equally acceptable.
 * </dd>
 * </dl>
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class DenialOfServiceConfiguration {
    @NotNull
    @JsonSetter(value = "enableFilter", nulls = Nulls.SKIP)
    private final Boolean enableFilter;

    @NotNull
    @Min(1)
    @JsonSetter(value = "maxRequestsPerSec", nulls = Nulls.SKIP)
    private final Long maxRequestsPerSec;

    @NotNull
    @Min(1)
    @JsonSetter(value = "maxRequestMs", nulls = Nulls.SKIP)
    private final Long maxRequestMs;

    @NotNull
    @Min(1)
    @JsonSetter(value = "maxWaitMs", nulls = Nulls.SKIP)
    private final Long maxWaitMs;

    @NotNull
    @Min(1)
    @JsonSetter(value = "throttledRequests", nulls = Nulls.SKIP)
    private final Long throttledRequests;

    @NotNull
    @JsonSetter(value = "delayMs", nulls = Nulls.SKIP)
    private final Long delayMs;

    @NotNull
    @JsonSetter(value = "ipWhitelist", nulls = Nulls.SKIP)
    private final String ipWhitelist;

    @NotNull
    @JsonSetter(value = "statusCode", nulls = Nulls.SKIP)
    private final String statusCode;


    /**
     * @param enableFilter Whether the filter is enabled or not
     * @param maxRequestsPerSec maximum requests allowed per second
     * @param maxRequestMs maximum amount of time (in milliseconds) to allow the request to process
     * @param maxWaitMs maximum amount of time (in milliseconds) the filter will block for the throttle semaphore
     * @param throttledRequests Set number of requests over the rate limit able to be considered at once
     * @param delayMs The delay given to all requests over the rate limit, before they are considered at all.

     */
    @JsonCreator
    DenialOfServiceConfiguration(@JsonProperty("enableFilter") final Boolean enableFilter,
                                 @JsonProperty("maxRequestsPerSec") final Long maxRequestsPerSec,
                                 @JsonProperty("maxRequestMs") final Long maxRequestMs,
                                 @JsonProperty("maxWaitMs") final Long maxWaitMs,
                                 @JsonProperty("throttledRequests") final Long throttledRequests,
                                 @JsonProperty("delayMs") final Long delayMs,
                                 @JsonProperty("ipWhitelist") final String ipWhitelist,
                                 @JsonProperty("statusCode") final String statusCode
                                 ) {
        this.enableFilter = enableFilter;
        this.maxRequestMs = maxRequestMs;
        this.maxWaitMs = maxWaitMs;
        this.throttledRequests = throttledRequests;
        this.maxRequestsPerSec = maxRequestsPerSec;
        this.delayMs = delayMs;
        this.ipWhitelist = ipWhitelist;
        this.statusCode = statusCode;
    }

    /**
     * Get the maximum requests per second
     *
     * @return maximum requests allowed per second
     */
    public Long getMaxRequestsPerSec() {
        return maxRequestsPerSec;
    }

    /**
     * Get the maximum amount of time (in milliseconds) to allow the request to process
     *
     * @return maximum amount of time (in milliseconds) to allow the request to process
     */
    public Long getMaxRequestMs() {
        return maxRequestMs;
    }

    /**
     * Get the delay given to all requests over the rate limit, before they are considered at all.
     *
     * @return The delay given to all requests over the rate limit, before they are considered at all.
     */
    public Long getDelayMs() {
        return delayMs;
    }

    /**
     * Get the maximum amount of time (in milliseconds) the filter will block for the throttle semaphore
     *
     * @return maximum amount of time (in milliseconds) the filter will block for the throttle semaphore
     */
    public Long getMaxWaitMs() {
        return maxWaitMs;
    }

    /**
     * Set number of requests over the rate limit able to be considered at once
     *
     * @return Set number of requests over the rate limit able to be considered at once
     */
    public Long getThrottledRequests() {
        return throttledRequests;
    }

    /**
     * Return whether the filter is enabled or not
     *
     * @return Whether the filter is enabled or not
     */
    public Boolean getEnableFilter() {
        return enableFilter;
    }

    /**
     * Get a comma-separated list of IP addresses that will not be rate limited
     *
     * @return A comma-separated list of IP addresses that will not be rate limited
     */
    public String getIpWhitelist() {
        return ipWhitelist;
    }

    /**
     * Get the status code to send if there are too many requests.
     *
     * @return The status code to send if there are too many requests.
     */
    public String getStatusCode() {
        return statusCode;
    }
}
