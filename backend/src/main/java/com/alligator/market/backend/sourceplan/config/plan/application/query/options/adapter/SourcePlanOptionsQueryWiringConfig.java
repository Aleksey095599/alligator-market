package com.alligator.market.backend.sourceplan.config.plan.application.query.options.adapter;

import com.alligator.market.backend.instrument.config.registry.InstrumentRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqMarketDataCapturerOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.adapter.JooqSourceOptionsQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCapturerOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.SourceOptionsQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(InstrumentRegistryWiringConfig.class)
public class SourcePlanOptionsQueryWiringConfig {
    public static final String BEAN_CAPTURER_OPTIONS_QUERY_PORT = "capturerOptionsQueryPort";
    public static final String BEAN_SOURCE_OPTIONS_QUERY_PORT = "sourceOptionsQueryPort";

    @Bean(BEAN_CAPTURER_OPTIONS_QUERY_PORT)
    public MarketDataCapturerOptionsQueryPort capturerOptionsQueryPort(DSLContext dsl) {
        return new JooqMarketDataCapturerOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_SOURCE_OPTIONS_QUERY_PORT)
    public SourceOptionsQueryPort sourceOptionsQueryPort(DSLContext dsl) {
        return new JooqSourceOptionsQueryAdapter(dsl);
    }
}
