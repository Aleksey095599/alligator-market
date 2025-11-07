package com.alligator.market.backend.config.audit.config;

import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Конфигурация JPA Auditing.
 */
@Configuration
@EnableJpaAuditing // ← Включаем инфраструктуру JPA-аудита
public class AuditConfig {

    /**
     * Поставщик текущего аудитора для Spring Data JPA.
     * Используется для заполнения полей {@link org.springframework.data.annotation.CreatedBy}
     * и {@link org.springframework.data.annotation.LastModifiedBy}.
     * Источник значения — {@link AuditContextHolder#actorOrFallback()} (возвращает фолбэк, если контекст не установлен).
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(AuditContextHolder.actorOrFallback());
    }
}
