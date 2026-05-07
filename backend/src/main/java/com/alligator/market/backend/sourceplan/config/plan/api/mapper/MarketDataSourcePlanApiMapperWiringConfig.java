package com.alligator.market.backend.sourceplan.config.plan.api.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.backend.sourceplan.plan.api.command.create.mapper.CreateMarketDataSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.api.command.replace.mapper.ReplaceMarketDataSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.MarketDataSourcePlanResponseMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.MarketDataSourceResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация API mapper-ов фичи sourcing.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePlanApiMapperWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_REQUEST_MAPPER = "marketDataSourceRequestMapper";
    public static final String BEAN_CREATE_MARKET_DATA_SOURCE_PLAN_MAPPER = "createMarketDataSourcePlanMapper";
    public static final String BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_MAPPER = "replaceMarketDataSourcePlanMapper";
    public static final String BEAN_MARKET_DATA_SOURCE_RESPONSE_MAPPER = "marketDataSourceResponseMapper";
    public static final String BEAN_MARKET_DATA_SOURCE_PLAN_RESPONSE_MAPPER = "marketDataSourcePlanResponseMapper";

    @Bean(BEAN_MARKET_DATA_SOURCE_REQUEST_MAPPER)
    public MarketDataSourceRequestMapper marketDataSourceRequestMapper() {
        return new MarketDataSourceRequestMapper();
    }

    @Bean(BEAN_CREATE_MARKET_DATA_SOURCE_PLAN_MAPPER)
    public CreateMarketDataSourcePlanMapper createMarketDataSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        return new CreateMarketDataSourcePlanMapper(marketDataSourceRequestMapper);
    }

    @Bean(BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_MAPPER)
    public ReplaceMarketDataSourcePlanMapper replaceMarketDataSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        return new ReplaceMarketDataSourcePlanMapper(marketDataSourceRequestMapper);
    }

    @Bean(BEAN_MARKET_DATA_SOURCE_RESPONSE_MAPPER)
    public MarketDataSourceResponseMapper marketDataSourceResponseMapper() {
        return new MarketDataSourceResponseMapper();
    }

    @Bean(BEAN_MARKET_DATA_SOURCE_PLAN_RESPONSE_MAPPER)
    public MarketDataSourcePlanResponseMapper marketDataSourcePlanResponseMapper(
            MarketDataSourceResponseMapper marketDataSourceResponseMapper
    ) {
        return new MarketDataSourcePlanResponseMapper(marketDataSourceResponseMapper);
    }
}
