package com.alligator.market.backend.marketdata.config.capture.process.persistence.passport.projection.port.adapter;

import com.alligator.market.backend.marketdata.capture.process.application.passport.projection.port.CaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.capture.process.persistence.passport.projection.port.adapter.JooqCaptureProcessPassportProjectionWritePortAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link CaptureProcessPassportProjectionWritePort}.
 */
@Configuration(proxyBeanMethods = false)
public class CaptureProcessPassportProjectionWritePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT =
            "captureProcessPassportProjectionWritePort";

    /**
     * Write-порт проекции паспортов процессов фиксации.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT)
    public CaptureProcessPassportProjectionWritePort captureProcessPassportProjectionWritePort(DSLContext dsl) {
        return new JooqCaptureProcessPassportProjectionWritePortAdapter(dsl);
    }
}
