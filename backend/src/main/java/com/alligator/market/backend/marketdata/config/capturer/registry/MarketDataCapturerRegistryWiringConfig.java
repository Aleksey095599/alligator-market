package com.alligator.market.backend.marketdata.config.capturer.registry;

import com.alligator.market.domain.marketdata.capturer.MarketDataCapturer;
import com.alligator.market.domain.marketdata.capturer.registry.MarketDataCapturerRegistry;
import com.alligator.market.domain.marketdata.capturer.registry.SnapshotMarketDataCapturerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link MarketDataCapturerRegistry}.
 *
 * <p>Spring внедряет {@code List<MarketDataCapturer>} как список всех бинов типа
 * {@link MarketDataCapturer}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCapturerRegistryWiringConfig {

    public static final String BEAN_CAPTURER_REGISTRY = "capturerRegistry";

    /**
     * Доменный snapshot-реестр процессов захвата.
     */
    @Bean(BEAN_CAPTURER_REGISTRY)
    public MarketDataCapturerRegistry capturerRegistry(List<MarketDataCapturer> capturers) {
        return new SnapshotMarketDataCapturerRegistry(capturers);
    }
}
