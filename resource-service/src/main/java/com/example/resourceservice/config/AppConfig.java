package com.example.resourceservice.config;

import io.micrometer.observation.ObservationPredicate;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

//    @Bean
//    ObservationRegistryCustomizer<ObservationRegistry> noSpringSecurityObservations() {
//        ObservationPredicate predicate = (name, context) -> {
//           return !context.getOrDefault("uriTemplate", "").contains("/actuator/prometheus");
//        };
//        return (registry) -> registry.observationConfig().observationPredicate(predicate);
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(customJwtAuthenticationConverter())
                ));
        return http.build();
    }

    @Bean
    public Converter<Jwt, JwtAuthenticationToken> customJwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter();
    }



}
