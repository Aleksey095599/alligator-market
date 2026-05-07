package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqMarketDataSourceExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataSourceExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourceExistencePortWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_EXISTENCE_PORT = "marketDataSourceExistencePort";

    @Bean(BEAN_MARKET_DATA_SOURCE_EXISTENCE_PORT)
    public MarketDataSourceExistencePort marketDataSourceExistencePort(DSLContext dsl) {
        return new JooqMarketDataSourceExistenceAdapter(dsl);
    }
}
