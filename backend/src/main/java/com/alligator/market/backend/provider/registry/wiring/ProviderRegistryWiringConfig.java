package com.alligator.market.backend.provider.registry.wiring;

import com.alligator.market.backend.provider.registry.ProviderRegistryAdapter;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация wiring {@link ProviderRegistry}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderRegistryWiringConfig {

    public static final String BEAN_NAME = "providerRegistry";

    /**
     * Бин {@link ProviderRegistry}.
     */
    @Bean(BEAN_NAME)
    public ProviderRegistry providerRegistry(List<MarketDataProvider> providers) {
        return new ProviderRegistryAdapter(providers);
    }

}
