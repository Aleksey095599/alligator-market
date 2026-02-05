package com.alligator.market.backend.provider.maintenance.projection.db.passport.config;

import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring для DB-проекции паспортов провайдеров.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportDbProjectionConfig {

    // TODO: уточнить зачем этот класс и нельзя ли его иначе назвать чем Config

    /**
     * Доменный use-case: обновление DB-проекции паспортов провайдеров по данным из контекста.
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
