package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.MDCaptureProcessCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqMDCaptureProcessCodeExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MDCaptureProcessCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MDCaptureProcessCodeExistencePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT = "captureProcessCodeExistencePort";

    @Bean(BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT)
    public MDCaptureProcessCodeExistencePort captureProcessCodeExistencePort(DSLContext dsl) {
        return new JooqMDCaptureProcessCodeExistenceAdapter(dsl);
    }
}
