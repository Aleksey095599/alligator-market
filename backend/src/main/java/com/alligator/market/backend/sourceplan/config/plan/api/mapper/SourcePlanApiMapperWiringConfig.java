package com.alligator.market.backend.sourceplan.config.plan.api.mapper;

import com.alligator.market.backend.sourceplan.plan.api.command.common.SourceRequestMapper;
import com.alligator.market.backend.sourceplan.plan.api.command.create.mapper.CreateSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.api.command.replace.mapper.ReplaceSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.SourcePlanResponseMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.SourceResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourcePlanApiMapperWiringConfig {
    public static final String BEAN_SOURCE_REQUEST_MAPPER = "sourceRequestMapper";
    public static final String BEAN_CREATE_SOURCE_PLAN_MAPPER = "createSourcePlanMapper";
    public static final String BEAN_REPLACE_SOURCE_PLAN_MAPPER = "replaceSourcePlanMapper";
    public static final String BEAN_SOURCE_RESPONSE_MAPPER = "sourceResponseMapper";
    public static final String BEAN_SOURCE_PLAN_RESPONSE_MAPPER = "sourcePlanResponseMapper";

    @Bean(BEAN_SOURCE_REQUEST_MAPPER)
    public SourceRequestMapper sourceRequestMapper() {
        return new SourceRequestMapper();
    }

    @Bean(BEAN_CREATE_SOURCE_PLAN_MAPPER)
    public CreateSourcePlanMapper createSourcePlanMapper(
            SourceRequestMapper sourceRequestMapper
    ) {
        return new CreateSourcePlanMapper(sourceRequestMapper);
    }

    @Bean(BEAN_REPLACE_SOURCE_PLAN_MAPPER)
    public ReplaceSourcePlanMapper replaceSourcePlanMapper(
            SourceRequestMapper sourceRequestMapper
    ) {
        return new ReplaceSourcePlanMapper(sourceRequestMapper);
    }

    @Bean(BEAN_SOURCE_RESPONSE_MAPPER)
    public SourceResponseMapper sourceResponseMapper() {
        return new SourceResponseMapper();
    }

    @Bean(BEAN_SOURCE_PLAN_RESPONSE_MAPPER)
    public SourcePlanResponseMapper sourcePlanResponseMapper(
            SourceResponseMapper sourceResponseMapper
    ) {
        return new SourcePlanResponseMapper(sourceResponseMapper);
    }
}
