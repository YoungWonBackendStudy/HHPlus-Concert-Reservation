package io.hhplus.concert.support.interceptor;

import io.hhplus.concert.domain.queue.ActiveToken;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.hhplus.concert.domain.queue.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor{
    TokenService tokenService;
    public TokenValidationInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull  HttpServletResponse response,@NonNull  Object handler) {
        String token = request.getHeader("TOKEN");
        tokenService.getActiveToken(token);
        return true;
    }
    
}
