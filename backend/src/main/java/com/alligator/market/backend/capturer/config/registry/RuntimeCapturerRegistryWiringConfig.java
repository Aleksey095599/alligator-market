package com.alligator.market.backend.capturer.config.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.registry.RuntimeCapturerRegistry;
import com.alligator.market.domain.capturer.registry.SnapshotRuntimeCapturerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RuntimeCapturerRegistryWiringConfig {
    public static final String BEAN_RUNTIME_CAPTURER_REGISTRY = "runtimeCapturerRegistry";

    @Bean(BEAN_RUNTIME_CAPTURER_REGISTRY)
    public RuntimeCapturerRegistry runtimeCapturerRegistry(List<MarketDataCapturer> capturers) {
        return new SnapshotRuntimeCapturerRegistry(capturers);
    }
}
