package com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository;

import com.alligator.market.backend.sourcing.plan.persistence.jooq.repository.JooqMarketDataSourcePlanRepositoryAdapter;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link MarketDataSourcePlanRepository}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePlanRepositoryWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY = "marketDataSourcePlanRepository";

    @Bean(BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
    public MarketDataSourcePlanRepository marketDataSourcePlanRepository(DSLContext dsl) {
        return new JooqMarketDataSourcePlanRepositoryAdapter(dsl);
    }
}
