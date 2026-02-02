package com.alligator.market.backend.provider.maintenance.config;

import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация бинов доменных сервисов для обслуживания (maintenance) провайдеров.
 */
@Configuration
public class ProviderMaintenanceConfig {

    /**
     * Бин доменного сервиса синхронизации провайдеров рыночных данных в контексте приложения и репозитории.
     */
    @Bean
    public ProviderPassportDbProjection providerSynchronizer(
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            ProviderPassportDbProjectionDao syncDao
    ) {
        return new ProviderPassportDbProjection(scanner, repository, syncDao);
    }
}
