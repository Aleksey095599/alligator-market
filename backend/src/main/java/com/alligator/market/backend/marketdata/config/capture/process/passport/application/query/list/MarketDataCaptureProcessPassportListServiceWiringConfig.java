package com.alligator.market.backend.marketdata.config.capture.process.passport.application.query.list;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.MarketDataCaptureProcessPassportListService;
import com.alligator.market.backend.marketdata.config.capture.process.registry.MarketDataCaptureProcessRegistryWiringConfig;
import com.alligator.market.domain.marketdata.capture.process.registry.MarketDataCaptureProcessRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MarketDataCaptureProcessPassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataCaptureProcessRegistryWiringConfig.class
})
public class MarketDataCaptureProcessPassportListServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE =
            "captureProcessPassportListService";

    /**
     * Сервис списка паспортов процессов захвата.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE)
    public MarketDataCaptureProcessPassportListService captureProcessPassportListService(
            @Qualifier(MarketDataCaptureProcessRegistryWiringConfig.BEAN_CAPTURE_PROCESS_REGISTRY)
            MarketDataCaptureProcessRegistry registry
    ) {
        return new MarketDataCaptureProcessPassportListService(registry);
    }
}
