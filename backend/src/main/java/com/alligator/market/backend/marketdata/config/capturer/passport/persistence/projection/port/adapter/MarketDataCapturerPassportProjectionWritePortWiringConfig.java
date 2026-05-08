package com.alligator.market.backend.marketdata.config.capturer.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.marketdata.capturer.passport.application.projection.port.MarketDataCapturerPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.capturer.passport.persistence.projection.port.adapter.JooqMarketDataCapturerPassportProjectionWritePortAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataCapturerPassportProjectionWritePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCapturerPassportProjectionWritePortWiringConfig {

    public static final String BEAN_CAPTURER_PASSPORT_PROJECTION_WRITE_PORT =
            "capturerPassportProjectionWritePort";

    /**
     * Write-порт проекции паспортов процессов захвата.
     */
    @Bean(BEAN_CAPTURER_PASSPORT_PROJECTION_WRITE_PORT)
    public MarketDataCapturerPassportProjectionWritePort capturerPassportProjectionWritePort(DSLContext dsl) {
        return new JooqMarketDataCapturerPassportProjectionWritePortAdapter(dsl);
    }
}
