package com.alligator.market.backend.provider.registry.wiring;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.registry.SnapshotProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация wiring {@link ProviderRegistry}.
 *
 * <p>Spring внедряет {@code List<MarketDataProvider>} как список всех бинов типа {@link MarketDataProvider}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class ProviderRegistryWiringConfig {

    public static final String BEAN_PROVIDER_REGISTRY = "providerRegistry";

    /**
     * Доменный snapshot-реестр провайдеров.
     */
    @Bean(BEAN_PROVIDER_REGISTRY)
    public ProviderRegistry providerRegistry(List<MarketDataProvider> providers) {
        return new SnapshotProviderRegistry(providers);
    }
}
