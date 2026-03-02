package com.alligator.market.backend.infra.jpa.audit.config;

import com.alligator.market.backend.infra.jpa.audit.context.AuditContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Конфигурационный класс инфраструктуры аудита:
 * <ul>
 *     <li>Активирует инфраструктуру Spring Data JPA auditing {@link EnableJpaAuditing};</li>
 *     <li>Задает бин поставщика текущего аудитора для Spring Data JPA.</li>
 * </ul>
 */
@Configuration
@EnableJpaAuditing
public class AuditConfig {

    /**
     * Поставщик текущего аудитора для инфраструктуры Spring JPA-аудита.
     *
     * <p>Использует {@link AuditContextHolder#actorOrFallback()} в качестве источника данных о текущем аудиторе.</p>
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(AuditContextHolder.actorOrFallback());
    }
}
