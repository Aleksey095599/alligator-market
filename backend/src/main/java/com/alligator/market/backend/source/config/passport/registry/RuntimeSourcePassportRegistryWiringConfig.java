package com.alligator.market.backend.source.config.passport.registry;

import com.alligator.market.backend.source.config.registry.RuntimeSourceRegistryWiringConfig;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistryAdapter;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeSourceRegistryWiringConfig.class)
public class RuntimeSourcePassportRegistryWiringConfig {
    public static final String BEAN_RUNTIME_SOURCE_PASSPORT_REGISTRY =
            "runtimeSourcePassportRegistry";

    @Bean(BEAN_RUNTIME_SOURCE_PASSPORT_REGISTRY)
    public RuntimeSourcePassportRegistry runtimeSourcePassportRegistry(
            @Qualifier(RuntimeSourceRegistryWiringConfig.BEAN_RUNTIME_SOURCE_REGISTRY)
            RuntimeSourceRegistry runtimeSourceRegistry
    ) {
        return new RuntimeSourcePassportRegistryAdapter(runtimeSourceRegistry);
    }
}
