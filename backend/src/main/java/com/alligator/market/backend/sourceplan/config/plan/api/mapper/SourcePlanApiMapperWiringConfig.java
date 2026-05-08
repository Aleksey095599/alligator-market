package com.alligator.market.backend.sourceplan.config.plan.api.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.backend.sourceplan.plan.api.command.create.mapper.CreateSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.api.command.replace.mapper.ReplaceSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.SourcePlanResponseMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.MarketDataSourceResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация API mapper-ов фичи sourcing.
 */
@Configuration(proxyBeanMethods = false)
public class SourcePlanApiMapperWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_REQUEST_MAPPER = "marketDataSourceRequestMapper";
    public static final String BEAN_CREATE_SOURCE_PLAN_MAPPER = "createSourcePlanMapper";
    public static final String BEAN_REPLACE_SOURCE_PLAN_MAPPER = "replaceSourcePlanMapper";
    public static final String BEAN_MARKET_DATA_SOURCE_RESPONSE_MAPPER = "marketDataSourceResponseMapper";
    public static final String BEAN_SOURCE_PLAN_RESPONSE_MAPPER = "sourcePlanResponseMapper";

    @Bean(BEAN_MARKET_DATA_SOURCE_REQUEST_MAPPER)
    public MarketDataSourceRequestMapper marketDataSourceRequestMapper() {
        return new MarketDataSourceRequestMapper();
    }

    @Bean(BEAN_CREATE_SOURCE_PLAN_MAPPER)
    public CreateSourcePlanMapper createSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        return new CreateSourcePlanMapper(marketDataSourceRequestMapper);
    }

    @Bean(BEAN_REPLACE_SOURCE_PLAN_MAPPER)
    public ReplaceSourcePlanMapper replaceSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        return new ReplaceSourcePlanMapper(marketDataSourceRequestMapper);
    }

    @Bean(BEAN_MARKET_DATA_SOURCE_RESPONSE_MAPPER)
    public MarketDataSourceResponseMapper marketDataSourceResponseMapper() {
        return new MarketDataSourceResponseMapper();
    }

    @Bean(BEAN_SOURCE_PLAN_RESPONSE_MAPPER)
    public SourcePlanResponseMapper sourcePlanResponseMapper(
            MarketDataSourceResponseMapper marketDataSourceResponseMapper
    ) {
        return new SourcePlanResponseMapper(marketDataSourceResponseMapper);
    }
}
