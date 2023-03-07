package net.kelsier.bookshelf.framework.filter;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.mockito.Mockito.times;

class CacheControlFilterTest {
    @Test
    @SuppressWarnings("UnnecessaryLocalVariable")
    void testDoFilter() throws Exception {
        final Request request = Mockito.mock(Request.class);
        final Response response = Mockito.mock(Response.class);
        final ServletRequest servletRequest = request;
        final ServletResponse servletResponse = response;
        final FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.doNothing().when(filterChain).doFilter(request, servletResponse);
        final CacheControlFilter cacheControlFilter = new CacheControlFilter();

        cacheControlFilter.init(null);
        cacheControlFilter.doFilter(servletRequest, servletResponse, filterChain);
        cacheControlFilter.destroy();

        Mockito.verify(response, times(1)).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        Mockito.verify(response, times(1)).setHeader("Pragma", "no-cache");
        Mockito.verify(response, times(1)).setHeader("Expires", "0");
    }
}