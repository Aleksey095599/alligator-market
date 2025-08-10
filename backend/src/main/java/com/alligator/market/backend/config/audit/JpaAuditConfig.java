package com.alligator.market.backend.config.audit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Конфигурация JPA-аудита.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    /** Имя сервиса для формирования аудитора. */
    @Bean
    public String serviceName(
            @Value("${auditing.service-id:${spring.application.name}}") String serviceName
    ) {
        return serviceName;
    }

    /** Провайдер аудитора на основе имени сервиса. */
    @Bean
    public AuditorAware<String> auditorProvider(String serviceName) {
        return new ContextAuditorAware(serviceName);
    }
}


