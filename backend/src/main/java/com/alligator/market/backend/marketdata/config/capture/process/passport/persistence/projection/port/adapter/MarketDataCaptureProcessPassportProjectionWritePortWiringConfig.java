package com.alligator.market.backend.marketdata.config.capture.process.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MarketDataCaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.capture.process.passport.persistence.projection.port.adapter.JooqMarketDataCaptureProcessPassportProjectionWritePortAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataCaptureProcessPassportProjectionWritePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCaptureProcessPassportProjectionWritePortWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT =
            "captureProcessPassportProjectionWritePort";

    /**
     * Write-порт проекции паспортов процессов захвата.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT)
    public MarketDataCaptureProcessPassportProjectionWritePort captureProcessPassportProjectionWritePort(DSLContext dsl) {
        return new JooqMarketDataCaptureProcessPassportProjectionWritePortAdapter(dsl);
    }
}
