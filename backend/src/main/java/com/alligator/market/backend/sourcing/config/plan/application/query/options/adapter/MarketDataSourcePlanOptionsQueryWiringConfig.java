package com.alligator.market.backend.sourcing.config.plan.application.query.options.adapter;

import com.alligator.market.backend.sourcing.plan.application.query.options.adapter.JooqInstrumentOptionsQueryAdapter;
import com.alligator.market.backend.sourcing.plan.application.query.options.adapter.JooqProviderOptionsQueryAdapter;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.ProviderOptionsQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link JooqInstrumentOptionsQueryAdapter}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePlanOptionsQueryWiringConfig {

    public static final String BEAN_INSTRUMENT_OPTIONS_QUERY_PORT = "instrumentOptionsQueryPort";
    public static final String BEAN_PROVIDER_OPTIONS_QUERY_PORT = "providerOptionsQueryPort";

    @Bean(BEAN_INSTRUMENT_OPTIONS_QUERY_PORT)
    public InstrumentOptionsQueryPort instrumentOptionsQueryPort(DSLContext dsl) {
        return new JooqInstrumentOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_PROVIDER_OPTIONS_QUERY_PORT)
    public ProviderOptionsQueryPort providerOptionsQueryPort(DSLContext dsl) {
        return new JooqProviderOptionsQueryAdapter(dsl);
    }
}
