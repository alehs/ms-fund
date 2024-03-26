package com.example.songservice.config;

import io.micrometer.observation.ObservationPredicate;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    ObservationRegistryCustomizer<ObservationRegistry> noActuatorObservation() {
        ObservationPredicate predicate = (name, context) -> {
            if (context.containsKey("uriTemplate")) {
                log.info("Observation handler for uriTemplate: {}", context.getOrDefault("uriTemplate", "null"));
            }
            return !context.getOrDefault("uriTemplate", "").contains("/actuator/prometheus");
        };
        return (registry) -> registry.observationConfig().observationPredicate(predicate);
    }
}
