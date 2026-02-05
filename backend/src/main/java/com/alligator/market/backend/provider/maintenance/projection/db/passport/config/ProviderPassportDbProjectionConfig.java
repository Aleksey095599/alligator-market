package com.alligator.market.backend.provider.maintenance.projection.db.passport.config;

import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring для доменного сервиса проекции паспортов провайдеров из контекста приложения в БД.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportDbProjectionConfig {

    /**
     * Доменный use-case: обновление проекции паспортов провайдеров в БД по данным из контекста.
     */
    @Bean
    public ProviderPassportDbProjection providerPassportDbProjection(
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            ProviderPassportDbProjectionDao projectionDao
    ) {
        return new ProviderPassportDbProjection(scanner, repository, projectionDao);
    }
}
