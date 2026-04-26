package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.reference.currency;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.adapter.JooqFxSpotCurrencyReferenceQueryAdapter;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port.FxSpotCurrencyReferenceQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация query-port ссылок FX_SPOT на валюту.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotCurrencyReferenceQueryWiringConfig {

    public static final String BEAN_FX_SPOT_CURRENCY_REFERENCE_QUERY_PORT = "fxSpotCurrencyReferenceQueryPort";

    @Bean(BEAN_FX_SPOT_CURRENCY_REFERENCE_QUERY_PORT)
    public FxSpotCurrencyReferenceQueryPort fxSpotCurrencyReferenceQueryPort(DSLContext dsl) {
        return new JooqFxSpotCurrencyReferenceQueryAdapter(dsl);
    }
}
