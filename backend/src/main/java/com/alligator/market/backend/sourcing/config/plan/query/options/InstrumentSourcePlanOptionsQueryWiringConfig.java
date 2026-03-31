package com.alligator.market.backend.sourcing.config.plan.query.options;

import com.alligator.market.backend.sourcing.plan.application.query.options.adapter.JooqInstrumentOptionsQueryAdapter;
import com.alligator.market.backend.sourcing.plan.application.query.options.adapter.JooqProviderOptionsQueryAdapter;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.ProviderOptionsQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring query-портов получения options для UI.
 */
@Configuration(proxyBeanMethods = false)
public class InstrumentSourcePlanOptionsQueryWiringConfig {

    public static final String BEAN_INSTRUMENT_OPTIONS_QUERY_PORT = "instrumentOptionsQueryPort";
    public static final String BEAN_PROVIDER_OPTIONS_QUERY_PORT = "providerOptionsQueryPort";

    /**
     * Query-порт получения доступных кодов инструментов.
     */
    @Bean(BEAN_INSTRUMENT_OPTIONS_QUERY_PORT)
    public InstrumentOptionsQueryPort instrumentOptionsQueryPort(DSLContext dsl) {
        return new JooqInstrumentOptionsQueryAdapter(dsl);
    }

    /**
     * Query-порт получения доступных кодов провайдеров.
     */
    @Bean(BEAN_PROVIDER_OPTIONS_QUERY_PORT)
    public ProviderOptionsQueryPort providerOptionsQueryPort(DSLContext dsl) {
        return new JooqProviderOptionsQueryAdapter(dsl);
    }
}
