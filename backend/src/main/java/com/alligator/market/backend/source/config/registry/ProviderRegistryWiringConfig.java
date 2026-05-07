package com.alligator.market.backend.source.config.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.ProviderRegistry;
import com.alligator.market.domain.source.registry.SnapshotProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link ProviderRegistry}.
 *
 * <p>Spring внедряет {@code List<MarketDataSource>} как список всех runtime source-бинов типа
 * {@link MarketDataSource}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class ProviderRegistryWiringConfig {

    public static final String BEAN_PROVIDER_REGISTRY = "providerRegistry";

    /**
     * Доменный snapshot-реестр провайдеров.
     */
    @Bean(BEAN_PROVIDER_REGISTRY)
    public ProviderRegistry providerRegistry(List<MarketDataSource> providers) {
        return new SnapshotProviderRegistry(providers);
    }
}
