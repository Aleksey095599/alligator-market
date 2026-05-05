package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqMarketDataCaptureProcessExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataCaptureProcessExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCaptureProcessExistencePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_EXISTENCE_PORT = "captureProcessExistencePort";

    @Bean(BEAN_CAPTURE_PROCESS_EXISTENCE_PORT)
    public MarketDataCaptureProcessExistencePort captureProcessExistencePort(DSLContext dsl) {
        return new JooqMarketDataCaptureProcessExistenceAdapter(dsl);
    }
}
