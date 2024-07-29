package io.hhplus.concert.support.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggingFiler implements Filter{
    static final long requestDurationWarning = 3000L;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!((request instanceof HttpServletRequest httpRequest) && (response instanceof HttpServletResponse httpResponse))) {
            throw new ServletException("OncePerRequestFilter only supports HTTP requests");
        }
        
        log.info("[REQ] Method: {}", httpRequest.getMethod());
        log.info("[REQ] URI: {}", httpRequest.getRequestURI());
        log.info("[REQ] Query Params: {}", httpRequest.getQueryString());

        long startTimeInMillis = System.currentTimeMillis();
        chain.doFilter(request, response);
        long requestDuration = System.currentTimeMillis() - startTimeInMillis;

        if(requestDuration >= requestDurationWarning) {
            log.warn("[RES] Duration: {}ms", requestDuration);
        }
        log.info("[RES] Status: {}", httpResponse.getStatus());
        log.info("[RES] Headers: {}", String.join(", ", httpResponse.getHeaderNames()));
    }
    
}
