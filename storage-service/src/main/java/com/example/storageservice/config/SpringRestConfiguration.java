package com.example.storageservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.Random;

@Slf4j
@Configuration
public class SpringRestConfiguration implements RepositoryRestConfigurer {

    private final Random random = new Random();

//    @Override
//    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
//        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
//        config.useHalAsDefaultJsonMediaType(false);
//    }

    @Bean
    public MappedInterceptor myMappedInterceptor() {
        return new MappedInterceptor(new String[]{"/api/**"}, new MyInterceptor());
    }

    private class MyInterceptor implements HandlerInterceptor {
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            if (random.nextInt(100) > 50)  {
                log.info("Random failure for request {}", request.getPathInfo());
                throw new IllegalStateException("Random service failure");
            }
            return true;
        }
    }
}