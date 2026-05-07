package com.alligator.market.backend.source.config.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotMarketDataSourceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link MarketDataSourceRegistry}.
 *
 * <p>Spring внедряет {@code List<MarketDataSource>} как список всех runtime source-бинов типа
 * {@link MarketDataSource}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourceRegistryWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_REGISTRY = "marketDataSourceRegistry";

    /**
     * Доменный snapshot-реестр провайдеров.
     */
    @Bean(BEAN_MARKET_DATA_SOURCE_REGISTRY)
    public MarketDataSourceRegistry marketDataSourceRegistry(List<MarketDataSource> sources) {
        return new SnapshotMarketDataSourceRegistry(sources);
    }
}
