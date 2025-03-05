package ar.edu.itba.paw.webapp.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

public class UnconditionalCacheFilter extends OncePerRequestFilter {
    public static final long MEDIA_MAX_AGE = Duration.ofMinutes(5).getSeconds();  // 5 minutes
    public static final long DEFAULT_MAX_AGE = Duration.ofDays(7).getSeconds(); // 1 week

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.GET.matches(httpServletRequest.getMethod())) {
            String requestURI = httpServletRequest.getRequestURI();
            long maxAge = requestURI.startsWith("/medias/") ? MEDIA_MAX_AGE : DEFAULT_MAX_AGE;

            httpServletResponse.setHeader(HttpHeaders.CACHE_CONTROL, String.format("public, max-age=%d, immutable", maxAge));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

