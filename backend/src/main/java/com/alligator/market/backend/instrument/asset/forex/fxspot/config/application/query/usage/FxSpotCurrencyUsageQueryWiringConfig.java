package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.usage;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.adapter.JooqFxSpotCurrencyUsageQueryAdapter;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.port.FxSpotCurrencyUsageQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация query-port проверки использования валюты в FOREX_SPOT.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotCurrencyUsageQueryWiringConfig {

    public static final String BEAN_FX_SPOT_CURRENCY_USAGE_QUERY_PORT = "fxSpotCurrencyUsageQueryPort";

    @Bean(BEAN_FX_SPOT_CURRENCY_USAGE_QUERY_PORT)
    public FxSpotCurrencyUsageQueryPort fxSpotCurrencyUsageQueryPort(DSLContext dsl) {
        return new JooqFxSpotCurrencyUsageQueryAdapter(dsl);
    }
}
