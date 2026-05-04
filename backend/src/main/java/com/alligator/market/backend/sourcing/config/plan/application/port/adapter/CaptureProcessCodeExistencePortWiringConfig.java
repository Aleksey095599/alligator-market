package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.CaptureProcessCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqCaptureProcessCodeExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link CaptureProcessCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class CaptureProcessCodeExistencePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT = "captureProcessCodeExistencePort";

    @Bean(BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT)
    public CaptureProcessCodeExistencePort captureProcessCodeExistencePort(DSLContext dsl) {
        return new JooqCaptureProcessCodeExistenceAdapter(dsl);
    }
}
