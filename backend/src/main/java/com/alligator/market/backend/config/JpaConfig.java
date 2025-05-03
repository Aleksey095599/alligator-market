package com.alligator.market.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Конфигурация JPA-аудита.
 */
@Configuration
@EnableJpaAuditing // ← включает обработку @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy
public class JpaConfig {

    /** Возвращает «кто совершил операцию». */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // TODO: временное решение, в дальнейшем брать из контекста Spring Security
        return () -> Optional.of("dev_admin");
    }

}


