package com.alligator.market.backend.marketdata.config.capture.process.application.passport.query.list;

import com.alligator.market.backend.marketdata.capture.process.application.passport.query.list.CaptureProcessPassportListService;
import com.alligator.market.backend.marketdata.config.capture.process.registry.CaptureProcessRegistryWiringConfig;
import com.alligator.market.domain.marketdata.capture.process.registry.CaptureProcessRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link CaptureProcessPassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CaptureProcessRegistryWiringConfig.class
})
public class CaptureProcessPassportListServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE =
            "captureProcessPassportListService";

    /**
     * Сервис списка паспортов процессов фиксации.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE)
    public CaptureProcessPassportListService captureProcessPassportListService(
            @Qualifier(CaptureProcessRegistryWiringConfig.BEAN_CAPTURE_PROCESS_REGISTRY)
            CaptureProcessRegistry registry
    ) {
        return new CaptureProcessPassportListService(registry);
    }
}
