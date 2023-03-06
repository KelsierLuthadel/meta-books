package net.kelsier.bookshelf.framework.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This filter applies CSRF protection for POST requests, other than ones that use application/json Content-Type.
 */
public class CsrfFilter implements Filter {
    public static final String CSRF_TOKEN_KEY = "csrf_token";
    private static final List<String> EXCLUDED_TYPES = List.of("application/json");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession();
        String csrfToken = (String) session.getAttribute(CSRF_TOKEN_KEY);
        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString().replace("-","");
            session.setAttribute(CSRF_TOKEN_KEY, csrfToken);
        }

        if("POST".equalsIgnoreCase(request.getMethod())
            && !EXCLUDED_TYPES.contains(request.getContentType())){
            // check for csrf
            final String csrfParameter = request.getParameter(CSRF_TOKEN_KEY);
            boolean matches = Objects.equals(csrfParameter, csrfToken);
            if(!matches) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

