package com.alligator.market.backend.source.config.passport.registry;

import com.alligator.market.backend.source.config.registry.RuntimeMarketDataSourceRegistryWiringConfig;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistryAdapter;
import com.alligator.market.domain.source.registry.RuntimeMarketDataSourceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeMarketDataSourceRegistryWiringConfig.class)
public class RuntimeSourcePassportRegistryWiringConfig {
    public static final String BEAN_RUNTIME_SOURCE_PASSPORT_REGISTRY =
            "runtimeSourcePassportRegistry";

    @Bean(BEAN_RUNTIME_SOURCE_PASSPORT_REGISTRY)
    public RuntimeSourcePassportRegistry runtimeSourcePassportRegistry(
            @Qualifier(RuntimeMarketDataSourceRegistryWiringConfig.BEAN_RUNTIME_MARKET_DATA_SOURCE_REGISTRY)
            RuntimeMarketDataSourceRegistry runtimeSourceRegistry
    ) {
        return new RuntimeSourcePassportRegistryAdapter(runtimeSourceRegistry);
    }
}
