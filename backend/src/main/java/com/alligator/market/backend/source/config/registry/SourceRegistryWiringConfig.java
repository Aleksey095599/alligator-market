package com.alligator.market.backend.source.config.registry;

import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.registry.SourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotSourceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class SourceRegistryWiringConfig {
    public static final String BEAN_SOURCE_REGISTRY = "sourceRegistry";

    @Bean(BEAN_SOURCE_REGISTRY)
    public SourceRegistry sourceRegistry(List<MarketSource> sources) {
        return new SnapshotSourceRegistry(sources);
    }
}
