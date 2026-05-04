package com.alligator.market.backend.marketdata.config.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MarketDataCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.registry.CaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.registry.SnapshotCaptureProcessRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link CaptureProcessRegistry}.
 *
 * <p>Spring внедряет {@code List<MarketDataCaptureProcess>} как список всех бинов типа
 * {@link MarketDataCaptureProcess}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class CaptureProcessRegistryWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_REGISTRY = "captureProcessRegistry";

    /**
     * Доменный snapshot-реестр процессов фиксации.
     */
    @Bean(BEAN_CAPTURE_PROCESS_REGISTRY)
    public CaptureProcessRegistry captureProcessRegistry(List<MarketDataCaptureProcess> processes) {
        return new SnapshotCaptureProcessRegistry(processes);
    }
}
