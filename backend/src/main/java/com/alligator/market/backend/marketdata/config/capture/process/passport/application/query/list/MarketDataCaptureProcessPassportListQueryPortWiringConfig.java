package com.alligator.market.backend.marketdata.config.capture.process.passport.application.query.list;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.adapter.JooqMarketDataCaptureProcessPassportListQueryAdapter;
import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.port.MarketDataCaptureProcessPassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring configuration for capture process passport list query port.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCaptureProcessPassportListQueryPortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_LIST_QUERY_PORT =
            "captureProcessPassportListQueryPort";

    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_LIST_QUERY_PORT)
    public MarketDataCaptureProcessPassportListQueryPort captureProcessPassportListQueryPort(DSLContext dsl) {
        return new JooqMarketDataCaptureProcessPassportListQueryAdapter(dsl);
    }
}
