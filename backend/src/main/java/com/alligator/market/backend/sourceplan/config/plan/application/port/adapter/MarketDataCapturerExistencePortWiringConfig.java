package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqMarketDataCapturerExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MarketDataCapturerExistencePortWiringConfig {
    public static final String BEAN_CAPTURER_EXISTENCE_PORT = "capturerExistencePort";

    @Bean(BEAN_CAPTURER_EXISTENCE_PORT)
    public MarketDataCapturerExistencePort capturerExistencePort(DSLContext dsl) {
        return new JooqMarketDataCapturerExistenceAdapter(dsl);
    }
}
