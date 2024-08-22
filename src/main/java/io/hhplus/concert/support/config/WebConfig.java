package io.hhplus.concert.support.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.hhplus.concert.support.filter.LoggingFiler;
import io.hhplus.concert.support.interceptor.TokenValidationInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    final TokenValidationInterceptor tokenValidationInterceptor;
    final LoggingFiler loggingFiler;


    public WebConfig(TokenValidationInterceptor tokenValidationInterceptor, LoggingFiler loggingFiler) {
        this.tokenValidationInterceptor = tokenValidationInterceptor;
        this.loggingFiler = loggingFiler;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor)
            .addPathPatterns("/concerts/**", "/reservations" , "/payment");
    }

    @Bean
    public FilterRegistrationBean<LoggingFiler> loggingFilter() {
        FilterRegistrationBean<LoggingFiler> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loggingFiler);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
