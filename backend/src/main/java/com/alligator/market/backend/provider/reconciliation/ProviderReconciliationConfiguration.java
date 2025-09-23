package com.alligator.market.backend.provider.reconciliation;

import com.alligator.market.domain.provider.reppository.ProviderDescriptorRepository;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import com.alligator.market.domain.provider.reconciliation.ProviderDescriptorSynchronizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурации бинов для доменных сервисов, используемых в процессах реконсиляции данных
 * о провайдерах рыночных данных.
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
