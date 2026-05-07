package com.alligator.market.backend.sourceplan.config.plan.application.query.common;

import com.alligator.market.backend.sourceplan.plan.application.query.common.adapter.JooqMarketDataSourcePlanQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.MarketDataSourcePlanQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация read-side запросов source plan для административного API.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePlanQueryPortWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_PLAN_QUERY_PORT =
            "marketDataSourcePlanQueryPort";

    @Bean(BEAN_MARKET_DATA_SOURCE_PLAN_QUERY_PORT)
    public MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort(DSLContext dsl) {
        return new JooqMarketDataSourcePlanQueryAdapter(dsl);
    }
}
