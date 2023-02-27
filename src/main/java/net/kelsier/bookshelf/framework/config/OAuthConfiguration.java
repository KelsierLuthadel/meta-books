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

package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Configuration for OAuth
 *
 * The following config parameters control the behavior of the OAuth Client:
 * <dl>
 * <dt>baseUrl</dt>
 * <dd> This defines the OAuth domain
 * </dd>
 *
 * </dl>
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings("UnusedAssignment")
public final class OAuthConfiguration {
    @NotNull
    @JsonSetter(value = "baseUrl", nulls = Nulls.SKIP)
    private final String baseUrl;

    @NotNull
    @JsonSetter(value = "issuer", nulls = Nulls.SKIP)
    private final String issuer;

    @NotNull
    @JsonSetter(value = "clientId", nulls = Nulls.SKIP)
    private final String clientId ;

    @NotNull
    @JsonSetter(value = "audience", nulls = Nulls.SKIP)
    private final String audience;

    /**
     * @param baseUrl
     * @param issuer
     * @param clientId
     * @param audience
     */
    @JsonCreator
    OAuthConfiguration(@JsonProperty("baseUrl") final String baseUrl,
                       @JsonProperty("issuer") final String issuer,
                       @JsonProperty("clientId") final String clientId,
                       @JsonProperty("audience") final String audience) {
        this.baseUrl = baseUrl;
        this.issuer = issuer;
        this.clientId = clientId;
        this.audience = audience;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAudience() {
        return audience;
    }
}
