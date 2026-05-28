package com.alligator.market.backend.source.config.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.RuntimeMarketDataSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotRuntimeMarketDataSourceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RuntimeMarketDataSourceRegistryWiringConfig {
    public static final String BEAN_RUNTIME_MARKET_DATA_SOURCE_REGISTRY =
            "runtimeMarketDataSourceRegistry";

    @Bean(BEAN_RUNTIME_MARKET_DATA_SOURCE_REGISTRY)
    public RuntimeMarketDataSourceRegistry runtimeMarketDataSourceRegistry(List<MarketDataSource> sources) {
        return new SnapshotRuntimeMarketDataSourceRegistry(sources);
    }
}
