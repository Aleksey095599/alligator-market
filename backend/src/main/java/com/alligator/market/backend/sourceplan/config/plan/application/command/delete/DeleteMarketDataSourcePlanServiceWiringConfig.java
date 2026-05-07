package com.alligator.market.backend.sourceplan.config.plan.application.command.delete;

import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.delete.DeleteMarketDataSourcePlanService;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link DeleteMarketDataSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanRepositoryWiringConfig.class
})
public class DeleteMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_DELETE_MARKET_DATA_SOURCE_PLAN_SERVICE = "deleteMarketDataSourcePlanService";

    @Bean(BEAN_DELETE_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public DeleteMarketDataSourcePlanService deleteMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository
    ) {
        return new DeleteMarketDataSourcePlanService(marketDataSourcePlanRepository);
    }
}
