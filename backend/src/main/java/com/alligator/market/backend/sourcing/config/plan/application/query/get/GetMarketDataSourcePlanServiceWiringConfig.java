package com.alligator.market.backend.sourcing.config.plan.application.query.get;

import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.query.get.GetMarketDataSourcePlanService;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link GetMarketDataSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanRepositoryWiringConfig.class
})
public class GetMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_GET_MARKET_DATA_SOURCE_PLAN_SERVICE = "getMarketDataSourcePlanService";

    @Bean(BEAN_GET_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public GetMarketDataSourcePlanService getMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository
    ) {
        return new GetMarketDataSourcePlanService(marketDataSourcePlanRepository);
    }
}
