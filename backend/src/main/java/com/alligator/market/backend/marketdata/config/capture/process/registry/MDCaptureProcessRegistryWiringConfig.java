package com.alligator.market.backend.marketdata.config.capture.process.registry;

import com.alligator.market.domain.marketdata.capture.process.MDCaptureProcess;
import com.alligator.market.domain.marketdata.capture.process.registry.MDCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.registry.SnapshotMDCaptureProcessRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link MDCaptureProcessRegistry}.
 *
 * <p>Spring внедряет {@code List<MDCaptureProcess>} как список всех бинов типа
 * {@link MDCaptureProcess}.</p>
 */
@Configuration(proxyBeanMethods = false)
public class MDCaptureProcessRegistryWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_REGISTRY = "captureProcessRegistry";

    /**
     * Доменный snapshot-реестр процессов захвата.
     */
    @Bean(BEAN_CAPTURE_PROCESS_REGISTRY)
    public MDCaptureProcessRegistry captureProcessRegistry(List<MDCaptureProcess> processes) {
        return new SnapshotMDCaptureProcessRegistry(processes);
    }
}
