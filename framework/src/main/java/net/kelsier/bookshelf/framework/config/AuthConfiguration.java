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

/**
 * Configuration for authentication
 * <p>
 * Only basic authentication is supported at the moment
 * </p>
 * The following config parameters control the behavior of authentication
 * <dl>
 * <dt>authType</dt>
 * <dd>The authentication type (only basic is supported)
 * </dd>
 * <dt>Realm</dt>
 * <dd> The realm
 * </dd>
 * </dl>
 *
 * @author Kelsier Luthadel
 * @version 1.0.0
 */
public final class AuthConfiguration {
    @JsonSetter(value = "authType", nulls = Nulls.SKIP)
    private final String authType;

    @JsonSetter(value = "realm", nulls = Nulls.SKIP)
    private final String realm;


    /**
     * @param authType - The auth type (only basic is supported)
     * @param realm - The realm

     */
    @JsonCreator
    AuthConfiguration(@JsonProperty("authType") final String authType,
                      @JsonProperty("realm") final String realm) {
        this.authType = authType;
        this.realm = realm;
    }

    /**
     * Get the auth type (basic)
     *
     * @return the auth type
     */
    public String getAuthType() {
        return authType;
    }

    /**
     * Get the realm
     *
     * @return the realm
     */
    public String getRealm() {
        return realm;
    }


}
