package com.alligator.market.backend.provider.reconciliation.config;

import com.alligator.market.domain.provider.repository.ProviderRepository;
import com.alligator.market.domain.provider.reconciliation.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация бинов доменных сервисов, используемых в процессе реконсиляции данных о провайдерах рыночных данных.
 */
@Configuration
public class ProviderReconciliationConfiguration {

    /** Бин доменного сервиса синхронизации дескрипторов провайдеров в контексте и репозитории. */
    @Bean
    public ProviderSynchronizer providerDescriptorSynchronizer(
            ProviderContextScanner scanner,
            ProviderRepository repository
    ) {
        return new ProviderSynchronizer(scanner, repository);
    }
}
