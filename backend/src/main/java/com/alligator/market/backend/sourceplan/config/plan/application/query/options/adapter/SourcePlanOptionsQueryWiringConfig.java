package com.alligator.market.backend.sourceplan.config.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqMarketDataCapturerOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqInstrumentOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqMarketDataSourceOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCapturerOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataSourceOptionsQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourcePlanOptionsQueryWiringConfig {
    public static final String BEAN_CAPTURER_OPTIONS_QUERY_PORT = "capturerOptionsQueryPort";
    public static final String BEAN_INSTRUMENT_OPTIONS_QUERY_PORT = "instrumentOptionsQueryPort";
    public static final String BEAN_MARKET_DATA_SOURCE_OPTIONS_QUERY_PORT = "marketDataSourceOptionsQueryPort";

    @Bean(BEAN_CAPTURER_OPTIONS_QUERY_PORT)
    public MarketDataCapturerOptionsQueryPort capturerOptionsQueryPort(DSLContext dsl) {
        return new JooqMarketDataCapturerOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_INSTRUMENT_OPTIONS_QUERY_PORT)
    public InstrumentOptionsQueryPort instrumentOptionsQueryPort(DSLContext dsl) {
        return new JooqInstrumentOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_MARKET_DATA_SOURCE_OPTIONS_QUERY_PORT)
    public MarketDataSourceOptionsQueryPort marketDataSourceOptionsQueryPort(DSLContext dsl) {
        return new JooqMarketDataSourceOptionsQueryAdapter(dsl);
    }
}
