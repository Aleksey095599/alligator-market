package com.alligator.market.backend.capturer.config.passport.registry;

import com.alligator.market.backend.capturer.config.registry.RuntimeCapturerRegistryWiringConfig;
import com.alligator.market.domain.capturer.passport.registry.CapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.registry.CapturerPassportRegistryAdapter;
import com.alligator.market.domain.capturer.registry.RuntimeCapturerRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeCapturerRegistryWiringConfig.class)
public class CapturerPassportRegistryWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_REGISTRY =
            "capturerPassportRegistry";

    @Bean(BEAN_CAPTURER_PASSPORT_REGISTRY)
    public CapturerPassportRegistry capturerPassportRegistry(
            @Qualifier(RuntimeCapturerRegistryWiringConfig.BEAN_RUNTIME_CAPTURER_REGISTRY)
            RuntimeCapturerRegistry runtimeCapturerRegistry
    ) {
        return new CapturerPassportRegistryAdapter(runtimeCapturerRegistry);
    }
}
