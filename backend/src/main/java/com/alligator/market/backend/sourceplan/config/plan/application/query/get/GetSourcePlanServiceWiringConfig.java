package com.alligator.market.backend.sourceplan.config.plan.application.query.get;

import com.alligator.market.backend.sourceplan.config.plan.application.query.common.SourcePlanQueryPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.get.GetSourcePlanService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePlanQueryPortWiringConfig.class
})
public class GetSourcePlanServiceWiringConfig {
    public static final String BEAN_GET_SOURCE_PLAN_SERVICE = "getSourcePlanService";

    @Bean(BEAN_GET_SOURCE_PLAN_SERVICE)
    public GetSourcePlanService getSourcePlanService(
            @Qualifier(SourcePlanQueryPortWiringConfig.BEAN_SOURCE_PLAN_QUERY_PORT)
            SourcePlanQueryPort sourcePlanQueryPort
    ) {
        return new GetSourcePlanService(sourcePlanQueryPort);
    }
}
