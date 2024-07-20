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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        
        log.info("[REQ] Method: {}", httpRequest.getMethod());
        log.info("[REQ] URI: {}", httpRequest.getRequestURI());
        log.info("[REQ] Query Params: {}", httpRequest.getQueryString());

        chain.doFilter(request, response);

        log.info("[RES] Status: {}", httpResponse.getStatus());
        log.info("[RES] Headers: {}", String.join(", ", httpResponse.getHeaderNames()));
    }
    
}
