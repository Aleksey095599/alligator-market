package com.alligator.market.backend.provider.reconciliation.config;

import com.alligator.market.domain.provider.reconciliation.passport.db.dao.ProviderPassportSyncDao;
import com.alligator.market.domain.provider.reconciliation.passport.service.ProviderPassportSynchronizer;
import com.alligator.market.domain.provider.reconciliation.context.scanner.ProviderContextScanner;
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
    public ProviderPassportSynchronizer providerSynchronizer(
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            ProviderPassportSyncDao syncDao
    ) {
        return new ProviderPassportSynchronizer(scanner, repository, syncDao);
    }
}
