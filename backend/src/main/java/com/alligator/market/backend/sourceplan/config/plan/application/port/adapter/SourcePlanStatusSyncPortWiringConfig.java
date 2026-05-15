package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.config.plan.registry.stored.StoredSourcePlanStatusPolicyWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqSourcePlanStatusSyncAdapter;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanStatusPolicy;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        StoredSourcePlanStatusPolicyWiringConfig.class
})
public class SourcePlanStatusSyncPortWiringConfig {
    public static final String BEAN_SOURCE_PLAN_STATUS_SYNC_PORT =
            "sourcePlanStatusSyncPort";

    @Bean(BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
    public SourcePlanStatusSyncPort sourcePlanStatusSyncPort(
            DSLContext dsl,
            @Qualifier(StoredSourcePlanStatusPolicyWiringConfig.BEAN_STORED_SOURCE_PLAN_STATUS_POLICY)
            StoredSourcePlanStatusPolicy statusPolicy
    ) {
        return new JooqSourcePlanStatusSyncAdapter(dsl, statusPolicy);
    }
}
