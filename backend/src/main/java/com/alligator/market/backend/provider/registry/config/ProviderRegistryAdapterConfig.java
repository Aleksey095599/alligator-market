package com.alligator.market.backend.provider.registry.config;

import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring {@link ProviderRegistryAdapter}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderRegistryAdapterConfig {
    
    /* Имя бина. */
    public static final String BEAN_PROVIDER_REGISTRY = "providerRegistry";

    /**
     * Бин {@link ProviderRegistry}.
     */
    @Bean(BEAN_PROVIDER_REGISTRY)
    public ProviderRegistry providerRegistry(){}

}
