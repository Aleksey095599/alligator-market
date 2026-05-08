package com.alligator.market.backend.source.config.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotMarketDataSourceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring configuration for {@link MarketDataSourceRegistry}.
 *
 * <p>Spring injects {@code List<MarketDataSource>} as all runtime source beans.</p>
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourceRegistryWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_REGISTRY = "marketDataSourceRegistry";

    /**
     * Domain snapshot registry of market data sources.
     */
    @Bean(BEAN_MARKET_DATA_SOURCE_REGISTRY)
    public MarketDataSourceRegistry marketDataSourceRegistry(List<MarketDataSource> sources) {
        return new SnapshotMarketDataSourceRegistry(sources);
    }
}
