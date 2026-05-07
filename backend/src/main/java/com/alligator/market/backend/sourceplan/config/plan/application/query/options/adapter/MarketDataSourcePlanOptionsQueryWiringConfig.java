package com.alligator.market.backend.sourceplan.config.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqMarketDataCaptureProcessOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqInstrumentOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqProviderOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCaptureProcessOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.ProviderOptionsQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация query-портов options для планов источников.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePlanOptionsQueryWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_OPTIONS_QUERY_PORT = "captureProcessOptionsQueryPort";
    public static final String BEAN_INSTRUMENT_OPTIONS_QUERY_PORT = "instrumentOptionsQueryPort";
    public static final String BEAN_PROVIDER_OPTIONS_QUERY_PORT = "providerOptionsQueryPort";

    @Bean(BEAN_CAPTURE_PROCESS_OPTIONS_QUERY_PORT)
    public MarketDataCaptureProcessOptionsQueryPort captureProcessOptionsQueryPort(DSLContext dsl) {
        return new JooqMarketDataCaptureProcessOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_INSTRUMENT_OPTIONS_QUERY_PORT)
    public InstrumentOptionsQueryPort instrumentOptionsQueryPort(DSLContext dsl) {
        return new JooqInstrumentOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_PROVIDER_OPTIONS_QUERY_PORT)
    public ProviderOptionsQueryPort providerOptionsQueryPort(DSLContext dsl) {
        return new JooqProviderOptionsQueryAdapter(dsl);
    }
}
