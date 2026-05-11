package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqSourcePlanStatusSyncAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourcePlanStatusSyncPortWiringConfig {
    public static final String BEAN_SOURCE_PLAN_STATUS_SYNC_PORT =
            "sourcePlanStatusSyncPort";

    @Bean(BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
    public SourcePlanStatusSyncPort sourcePlanStatusSyncPort(DSLContext dsl) {
        return new JooqSourcePlanStatusSyncAdapter(dsl);
    }
}
