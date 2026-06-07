package com.alligator.market.backend.capturer.config.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.registry.CapturerRegistry;
import com.alligator.market.domain.capturer.registry.SnapshotCapturerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class CapturerRegistryWiringConfig {
    public static final String BEAN_CAPTURER_REGISTRY = "capturerRegistry";

    @Bean(BEAN_CAPTURER_REGISTRY)
    public CapturerRegistry capturerRegistry(List<MarketDataCapturer> capturers) {
        return new SnapshotCapturerRegistry(capturers);
    }
}
