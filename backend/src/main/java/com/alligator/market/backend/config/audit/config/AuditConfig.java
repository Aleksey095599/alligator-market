package com.alligator.market.backend.config.audit.config;

import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Конфигурация JPA Auditing.
 */
@Configuration
@EnableJpaAuditing // <-- Включаем инфраструктуру JPA-аудита
public class AuditConfig {

    /**
     * Поставщик текущего аудитора для Spring Data JPA.
     *
     * <p>Используется для заполнения полей {@link CreatedBy} и {@link LastModifiedBy}.
     * Источник значений для полей – {@link AuditContextHolder#actorOrFallback()}.</p>
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(AuditContextHolder.actorOrFallback());
    }
}
