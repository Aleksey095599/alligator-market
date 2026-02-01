package com.alligator.market.backend.provider.maintenance.config;

import com.alligator.market.domain.provider.maintenance.passport.sync.dao.PassportDbSyncDao;
import com.alligator.market.domain.provider.maintenance.passport.sync.service.PassportDbSync;
import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация бинов доменных сервисов, используемых в процессе реконсиляции данных о провайдерах рыночных данных.
 */
@Configuration
public class ProviderReconciliationConfiguration {

    /**
     * Бин доменного сервиса синхронизации провайдеров рыночных данных в контексте приложения и репозитории.
     */
    @Bean
    public PassportDbSync providerSynchronizer(
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            PassportDbSyncDao syncDao
    ) {
        return new PassportDbSync(scanner, repository, syncDao);
    }
}
