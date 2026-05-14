package com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.registry;

import com.alligator.market.backend.sourceplan.plan.persistence.jooq.registry.JooqStoredSourcePlanRegistryAdapter;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StoredSourcePlanRegistryWiringConfig {
    public static final String BEAN_STORED_SOURCE_PLAN_REGISTRY = "storedSourcePlanRegistry";

    @Bean(BEAN_STORED_SOURCE_PLAN_REGISTRY)
    public StoredSourcePlanRegistry storedSourcePlanRegistry(DSLContext dsl) {
        return new JooqStoredSourcePlanRegistryAdapter(dsl);
    }
}
