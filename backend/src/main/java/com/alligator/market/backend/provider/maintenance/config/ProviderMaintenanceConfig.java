package com.alligator.market.backend.provider.maintenance.config;

import com.alligator.market.domain.provider.maintenance.sync.passport.dao.ProviderPassportDbSyncDao;
import com.alligator.market.domain.provider.maintenance.sync.passport.service.ProviderPassportDbSync;
import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация бинов доменных сервисов, используемых в процессе реконсиляции данных о провайдерах рыночных данных.
 */
@Configuration
public class ProviderMaintenanceConfig {

    /**
     * Бин доменного сервиса синхронизации провайдеров рыночных данных в контексте приложения и репозитории.
     */
    @Bean
    public ProviderPassportDbSync providerSynchronizer(
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            ProviderPassportDbSyncDao syncDao
    ) {
        return new ProviderPassportDbSync(scanner, repository, syncDao);
    }
}
