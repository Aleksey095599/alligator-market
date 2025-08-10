package com.alligator.market.backend.config.audit.old;

import com.alligator.market.backend.config.audit.AuditContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Конфигурация аудита: включает JPA Auditing и задаёт текущего аудитора.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    /** Поставщик для @CreatedBy/@LastModifiedBy. */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(AuditContextHolder.get().actorId());
    }
}


