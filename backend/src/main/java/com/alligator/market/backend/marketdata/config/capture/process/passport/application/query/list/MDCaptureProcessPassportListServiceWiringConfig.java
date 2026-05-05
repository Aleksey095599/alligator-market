package com.alligator.market.backend.marketdata.config.capture.process.passport.application.query.list;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.MDCaptureProcessPassportListService;
import com.alligator.market.backend.marketdata.config.capture.process.registry.MDCaptureProcessRegistryWiringConfig;
import com.alligator.market.domain.marketdata.capture.process.registry.MDCaptureProcessRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MDCaptureProcessPassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MDCaptureProcessRegistryWiringConfig.class
})
public class MDCaptureProcessPassportListServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE =
            "captureProcessPassportListService";

    /**
     * Сервис списка паспортов процессов захвата.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE)
    public MDCaptureProcessPassportListService captureProcessPassportListService(
            @Qualifier(MDCaptureProcessRegistryWiringConfig.BEAN_CAPTURE_PROCESS_REGISTRY)
            MDCaptureProcessRegistry registry
    ) {
        return new MDCaptureProcessPassportListService(registry);
    }
}
