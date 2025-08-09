package com.alligator.market.backend.config.audit;

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

    /** Провайдер аудитора на основе контекста сервиса. */
    @Bean
    public AuditorAware<String> auditorProvider(ServiceAuditorContext context) {
        return new ContextAuditorAware(context);
    }
}


