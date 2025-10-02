package com.alligator.market.backend.provider.reconciliation.config;

import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;
import com.alligator.market.domain.provider.reconciliation.scaner.ProviderContextScanner;
import com.alligator.market.domain.provider.reconciliation.descriptor.ProviderDescriptorSynchronizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация бинов доменных сервисов, используемых в процессе реконсиляции данных о провайдерах рыночных данных.
 */
@Configuration
public class ProviderReconciliationConfiguration {

    /** Бин доменного сервиса синхронизации дескрипторов провайдеров в контексте и репозитории. */
    @Bean
    public ProviderDescriptorSynchronizer providerDescriptorSynchronizer(
            ProviderContextScanner scanner,
            ProviderDescriptorRepository repository
    ) {
        return new ProviderDescriptorSynchronizer(scanner, repository);
    }
}
