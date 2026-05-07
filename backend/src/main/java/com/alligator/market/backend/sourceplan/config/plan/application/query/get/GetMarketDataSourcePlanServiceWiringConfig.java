package com.alligator.market.backend.sourceplan.config.plan.application.query.get;

import com.alligator.market.backend.sourceplan.config.plan.application.query.common.MarketDataSourcePlanQueryPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.MarketDataSourcePlanQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.get.GetMarketDataSourcePlanService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanQueryPortWiringConfig.class
})
public class GetMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_GET_MARKET_DATA_SOURCE_PLAN_SERVICE = "getMarketDataSourcePlanService";

    @Bean(BEAN_GET_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public GetMarketDataSourcePlanService getMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanQueryPortWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_QUERY_PORT)
            MarketDataSourcePlanQueryPort marketDataSourcePlanQueryPort
    ) {
        return new GetMarketDataSourcePlanService(marketDataSourcePlanQueryPort);
    }
}
