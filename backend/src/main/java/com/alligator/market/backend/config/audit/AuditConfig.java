package com.alligator.market.backend.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *  Конфигурация аудита.
 *  Включает JPA Auditing и отдаёт текущего автора из AuditContextHolder.
 */
@Configuration
@EnableJpaAuditing // ← Включаем инфраструктуру JPA-аудита
public class AuditConfig {

    /* Значение фолбэк для актора (если актор или контекст не задан). */
    private static final String FALLBACK_ACTOR = "DEFAULT";

    /** Бин-поставщик данных для @CreatedBy/@LastModifiedBy из AuditContextHolder. */
    @Bean
    public org.springframework.data.domain.AuditorAware<String> auditorAware() {
        return () -> java.util.Optional.ofNullable(
                        com.alligator.market.backend.config.audit.AuditContextHolder.get()
                )
                .map(com.alligator.market.backend.config.audit.AuditContext::actorId)
                .filter(a -> !a.isBlank())                  // ← пустые значения игнорируем
                .or(() -> java.util.Optional.of(FALLBACK_ACTOR)); // ← фолбэк актор
    }
}
