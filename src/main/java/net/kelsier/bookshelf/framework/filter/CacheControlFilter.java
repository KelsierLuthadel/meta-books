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

package net.kelsier.bookshelf.framework.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A Filter to allow caching or non-caching of response data
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public class CacheControlFilter implements Filter {
    /**
     * constructor
     */
    @Override
    public void init(final FilterConfig filterConfig) {
        // This is here for completeness
    }

    /**
     * Filter requests to return Cache-Control headers back to the caller,
     * this is required for API responses.
     * @param servletRequest client request
     * @param servletResponse client response
     * @param filterChain  invocation chain of a filtered request for a resource
     * @throws IOException thrown when an I/O exception occurs
     * @throws ServletException thrown when a servlet encounters an error
     */
    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Expires", "0");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * called on shutdown
     */
    @Override
    public void destroy() {
        // This is here for completeness
    }
}
