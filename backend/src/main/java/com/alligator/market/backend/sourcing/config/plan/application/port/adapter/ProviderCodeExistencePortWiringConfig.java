package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.adapter.RegistryProviderCodeExistenceAdapter;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link ProviderCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
@Import(ProviderRegistryWiringConfig.class)
public class ProviderCodeExistencePortWiringConfig {

    public static final String BEAN_PROVIDER_CODE_EXISTENCE_PORT = "providerCodeExistencePort";

    @Bean(BEAN_PROVIDER_CODE_EXISTENCE_PORT)
    public ProviderCodeExistencePort providerCodeExistencePort(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry
    ) {
        return new RegistryProviderCodeExistenceAdapter(providerRegistry);
    }
}
