package com.alligator.market.backend.sourcing.config.plan.application.query.list;

import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.query.list.MarketDataSourcePlanListService;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MarketDataSourcePlanListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanRepositoryWiringConfig.class
})
public class ListMarketDataSourcePlansServiceWiringConfig {

    public static final String BEAN_LIST_MARKET_DATA_SOURCE_PLANS_SERVICE = "listMarketDataSourcePlansService";

    @Bean(BEAN_LIST_MARKET_DATA_SOURCE_PLANS_SERVICE)
    public MarketDataSourcePlanListService listMarketDataSourcePlansService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository
    ) {
        return new MarketDataSourcePlanListService(marketDataSourcePlanRepository);
    }
}
