package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqMarketDataCaptureProcessCodeExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataCaptureProcessCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCaptureProcessCodeExistencePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT = "captureProcessCodeExistencePort";

    @Bean(BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT)
    public MarketDataCaptureProcessCodeExistencePort captureProcessCodeExistencePort(DSLContext dsl) {
        return new JooqMarketDataCaptureProcessCodeExistenceAdapter(dsl);
    }
}
