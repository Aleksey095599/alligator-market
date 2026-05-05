package com.alligator.market.backend.marketdata.config.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.registry.MarketDataCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.registry.SnapshotMarketDataCaptureProcessRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link MarketDataCaptureProcessRegistry}.
 *
 * <p>Spring внедряет {@code List<MarketDataCaptureProcess>} как список всех бинов типа
 * {@link MarketDataCaptureProcess}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCaptureProcessRegistryWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_REGISTRY = "captureProcessRegistry";

    /**
     * Доменный snapshot-реестр процессов захвата.
     */
    @Bean(BEAN_CAPTURE_PROCESS_REGISTRY)
    public MarketDataCaptureProcessRegistry captureProcessRegistry(List<MarketDataCaptureProcess> processes) {
        return new SnapshotMarketDataCaptureProcessRegistry(processes);
    }
}
