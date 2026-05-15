package com.alligator.market.backend.sourceplan.config.plan.registry.stored;

import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanStatusPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StoredSourcePlanStatusPolicyWiringConfig {
    public static final String BEAN_STORED_SOURCE_PLAN_STATUS_POLICY =
            "storedSourcePlanStatusPolicy";

    @Bean(BEAN_STORED_SOURCE_PLAN_STATUS_POLICY)
    public StoredSourcePlanStatusPolicy storedSourcePlanStatusPolicy() {
        return new StoredSourcePlanStatusPolicy();
    }
}
