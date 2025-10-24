package com.alligator.market.backend.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Конфигурация аудита.
 * Включает JPA Auditing и отдаёт текущего автора из AuditContextHolder.
 */
@Configuration
@EnableJpaAuditing // ← Включаем инфраструктуру JPA-аудита
public class AuditConfig {

    /** Бин поставщик данных для @CreatedBy/@LastModifiedBy из AuditContextHolder. */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(AuditContextHolder.actorOrFallback());
    }
}
