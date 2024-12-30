package com.example.prello.config;

import com.example.prello.security.filter.LoginFilter;
import com.example.prello.security.interceptor.AdminAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String[] ADMIN_REQUIRED_PATHS = {"/api/workspaces", "/api/workspaces/*/members/*"};

    private final AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns(ADMIN_REQUIRED_PATHS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);
    }

    @Bean
    public FilterRegistrationBean<LoginFilter> authenticationFilter() {
        FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.addUrlPatterns("/api/*");
        return filterRegistrationBean;
    }
}