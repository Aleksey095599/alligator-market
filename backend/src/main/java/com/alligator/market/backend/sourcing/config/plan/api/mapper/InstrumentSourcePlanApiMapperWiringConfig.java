package com.alligator.market.backend.sourcing.config.plan.api.mapper;

import com.alligator.market.backend.sourcing.plan.api.command.common.MarketDataSourceRequestMapper;
import com.alligator.market.backend.sourcing.plan.api.command.create.mapper.CreateInstrumentSourcePlanMapper;
import com.alligator.market.backend.sourcing.plan.api.command.replace.mapper.ReplaceInstrumentSourcePlanMapper;
import com.alligator.market.backend.sourcing.plan.api.query.common.mapper.InstrumentSourcePlanResponseMapper;
import com.alligator.market.backend.sourcing.plan.api.query.common.mapper.MarketDataSourceResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация API mapper-ов фичи sourcing/plan.
 */
@Configuration(proxyBeanMethods = false)
public class InstrumentSourcePlanApiMapperWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_REQUEST_MAPPER = "marketDataSourceRequestMapper";
    public static final String BEAN_CREATE_INSTRUMENT_SOURCE_PLAN_MAPPER = "createInstrumentSourcePlanMapper";
    public static final String BEAN_REPLACE_INSTRUMENT_SOURCE_PLAN_MAPPER = "replaceInstrumentSourcePlanMapper";
    public static final String BEAN_MARKET_DATA_SOURCE_RESPONSE_MAPPER = "marketDataSourceResponseMapper";
    public static final String BEAN_INSTRUMENT_SOURCE_PLAN_RESPONSE_MAPPER = "instrumentSourcePlanResponseMapper";

    @Bean(BEAN_MARKET_DATA_SOURCE_REQUEST_MAPPER)
    public MarketDataSourceRequestMapper marketDataSourceRequestMapper() {
        return new MarketDataSourceRequestMapper();
    }

    @Bean(BEAN_CREATE_INSTRUMENT_SOURCE_PLAN_MAPPER)
    public CreateInstrumentSourcePlanMapper createInstrumentSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        return new CreateInstrumentSourcePlanMapper(marketDataSourceRequestMapper);
    }

    @Bean(BEAN_REPLACE_INSTRUMENT_SOURCE_PLAN_MAPPER)
    public ReplaceInstrumentSourcePlanMapper replaceInstrumentSourcePlanMapper(
            MarketDataSourceRequestMapper marketDataSourceRequestMapper
    ) {
        return new ReplaceInstrumentSourcePlanMapper(marketDataSourceRequestMapper);
    }

    @Bean(BEAN_MARKET_DATA_SOURCE_RESPONSE_MAPPER)
    public MarketDataSourceResponseMapper marketDataSourceResponseMapper() {
        return new MarketDataSourceResponseMapper();
    }

    @Bean(BEAN_INSTRUMENT_SOURCE_PLAN_RESPONSE_MAPPER)
    public InstrumentSourcePlanResponseMapper instrumentSourcePlanResponseMapper(
            MarketDataSourceResponseMapper marketDataSourceResponseMapper
    ) {
        return new InstrumentSourcePlanResponseMapper(marketDataSourceResponseMapper);
    }
}
