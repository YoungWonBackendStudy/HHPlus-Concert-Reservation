package io.hhplus.concert.support.interceptor;

import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.domain.queue.ActiveToken;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.hhplus.concert.domain.queue.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class TokenValidationInterceptor implements HandlerInterceptor{
    private final QueueFacade queueFacade;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull  HttpServletResponse response,@NonNull  Object handler) {
        String token = request.getHeader("TOKEN");
        queueFacade.getActiveToken(token);
        return true;
    }
    
}
