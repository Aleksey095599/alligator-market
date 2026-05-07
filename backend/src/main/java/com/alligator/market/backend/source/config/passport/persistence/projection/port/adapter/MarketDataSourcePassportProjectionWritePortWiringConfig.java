package com.alligator.market.backend.source.config.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.source.passport.application.projection.port.MarketDataSourcePassportProjectionWritePort;
import com.alligator.market.backend.source.passport.persistence.projection.port.adapter.JooqMarketDataSourcePassportProjectionWritePortAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataSourcePassportProjectionWritePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePassportProjectionWritePortWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_WRITE_PORT =
            "marketDataSourcePassportProjectionWritePort";

    /**
     * Write-порт проекции паспортов провайдеров.
     */
    @Bean(BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_WRITE_PORT)
    public MarketDataSourcePassportProjectionWritePort marketDataSourcePassportProjectionWritePort(DSLContext dsl) {
        return new JooqMarketDataSourcePassportProjectionWritePortAdapter(dsl);
    }
}
