package com.alligator.market.backend.source.config.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotRuntimeSourceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RuntimeSourceRegistryWiringConfig {
    public static final String BEAN_RUNTIME_SOURCE_REGISTRY =
            "runtimeSourceRegistry";

    @Bean(BEAN_RUNTIME_SOURCE_REGISTRY)
    public RuntimeSourceRegistry runtimeSourceRegistry(List<MarketDataSource> sources) {
        return new SnapshotRuntimeSourceRegistry(sources);
    }
}
