package com.alligator.market.backend.marketdata.config.capture.process.passport.application.query.list;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.MarketDataCaptureProcessPassportListService;
import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.port.MarketDataCaptureProcessPassportListQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring configuration for {@link MarketDataCaptureProcessPassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataCaptureProcessPassportListQueryPortWiringConfig.class
})
public class MarketDataCaptureProcessPassportListServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE =
            "captureProcessPassportListService";

    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_LIST_SERVICE)
    public MarketDataCaptureProcessPassportListService captureProcessPassportListService(
            @Qualifier(MarketDataCaptureProcessPassportListQueryPortWiringConfig
                    .BEAN_CAPTURE_PROCESS_PASSPORT_LIST_QUERY_PORT)
            MarketDataCaptureProcessPassportListQueryPort queryPort
    ) {
        return new MarketDataCaptureProcessPassportListService(queryPort);
    }
}
