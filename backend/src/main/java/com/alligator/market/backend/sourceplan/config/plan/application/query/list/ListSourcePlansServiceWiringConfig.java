package com.alligator.market.backend.sourceplan.config.plan.application.query.list;

import com.alligator.market.backend.sourceplan.config.plan.application.query.common.SourcePlanQueryPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.list.SourcePlanListService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePlanQueryPortWiringConfig.class
})
public class ListSourcePlansServiceWiringConfig {
    public static final String BEAN_LIST_SOURCE_PLANS_SERVICE = "listSourcePlansService";

    @Bean(BEAN_LIST_SOURCE_PLANS_SERVICE)
    public SourcePlanListService listSourcePlansService(
            @Qualifier(SourcePlanQueryPortWiringConfig.BEAN_SOURCE_PLAN_QUERY_PORT)
            SourcePlanQueryPort sourcePlanQueryPort
    ) {
        return new SourcePlanListService(sourcePlanQueryPort);
    }
}
