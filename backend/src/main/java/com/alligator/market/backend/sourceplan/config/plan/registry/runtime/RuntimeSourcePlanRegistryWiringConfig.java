package com.alligator.market.backend.sourceplan.config.plan.registry.runtime;

import com.alligator.market.backend.sourceplan.plan.registry.runtime.AtomicRuntimeSourcePlanRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RuntimeSourcePlanRegistryWiringConfig {
    public static final String BEAN_RUNTIME_SOURCE_PLAN_REGISTRY =
            "runtimeSourcePlanRegistry";
    public static final String BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_PUBLISHER =
            BEAN_RUNTIME_SOURCE_PLAN_REGISTRY;

    @Bean(BEAN_RUNTIME_SOURCE_PLAN_REGISTRY)
    public AtomicRuntimeSourcePlanRegistry runtimeSourcePlanRegistry() {
        return new AtomicRuntimeSourcePlanRegistry();
    }
}
