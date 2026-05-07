package com.alligator.market.backend.sourceplan.config.plan.application.query.list;

import com.alligator.market.backend.sourceplan.config.plan.application.query.common.MarketDataSourcePlanQueryPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.MarketDataSourcePlanQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.list.MarketDataSourcePlanListService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanQueryPortWiringConfig.class
})
public class ListMarketDataSourcePlansServiceWiringConfig {

    public static final String BEAN_LIST_MARKET_DATA_SOURCE_PLANS_SERVICE = "listMarketDataSourcePlansService";

    @Bean(BEAN_LIST_MARKET_DATA_SOURCE_PLANS_SERVICE)
    public MarketDataSourcePlanListService listMarketDataSourcePlansService(
            @Qualifier(MarketDataSourcePlanQueryPortWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_QUERY_PORT)
            MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort
    ) {
        return new MarketDataSourcePlanListService(marketDataSourcePlanQueryPort);
    }
}
