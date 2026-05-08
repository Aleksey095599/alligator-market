package com.alligator.market.backend.capturer.config.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.registry.MarketDataCapturerRegistry;
import com.alligator.market.domain.capturer.registry.SnapshotMarketDataCapturerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class MarketDataCapturerRegistryWiringConfig {
    public static final String BEAN_CAPTURER_REGISTRY = "capturerRegistry";

    @Bean(BEAN_CAPTURER_REGISTRY)
    public MarketDataCapturerRegistry capturerRegistry(List<MarketDataCapturer> capturers) {
        return new SnapshotMarketDataCapturerRegistry(capturers);
    }
}
