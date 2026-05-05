package com.alligator.market.backend.marketdata.config.capture.process.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MDCaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.capture.process.passport.persistence.projection.port.adapter.JooqMDCaptureProcessPassportProjectionWritePortAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MDCaptureProcessPassportProjectionWritePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MDCaptureProcessPassportProjectionWritePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT =
            "captureProcessPassportProjectionWritePort";

    /**
     * Write-порт проекции паспортов процессов фиксации.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT)
    public MDCaptureProcessPassportProjectionWritePort captureProcessPassportProjectionWritePort(DSLContext dsl) {
        return new JooqMDCaptureProcessPassportProjectionWritePortAdapter(dsl);
    }
}
