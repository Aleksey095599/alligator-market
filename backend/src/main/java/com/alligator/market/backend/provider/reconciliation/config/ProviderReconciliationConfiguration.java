package com.alligator.market.backend.provider.reconciliation.config;

import com.alligator.market.domain.provider.reconciliation.ProviderSyncDao;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import com.alligator.market.domain.provider.reconciliation.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.ProviderPassportRepository;
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
    public ProviderSynchronizer providerSynchronizer(
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            ProviderSyncDao syncDao
    ) {
        return new ProviderSynchronizer(scanner, repository, syncDao);
    }
}
