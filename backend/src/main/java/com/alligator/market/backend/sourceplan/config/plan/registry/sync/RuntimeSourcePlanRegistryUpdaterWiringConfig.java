package com.alligator.market.backend.sourceplan.config.plan.registry.sync;

import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.registry.StoredSourcePlanRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.runtime.RuntimeSourcePlanRegistryWiringConfig;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanRegistry;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryPublisher;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import com.alligator.market.domain.sourceplan.registry.sync.SnapshotRuntimeSourcePlanRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        StoredSourcePlanRegistryWiringConfig.class,
        RuntimeSourcePlanRegistryWiringConfig.class
})
public class RuntimeSourcePlanRegistryUpdaterWiringConfig {
    public static final String BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_UPDATER =
            "runtimeSourcePlanRegistryUpdater";

    @Bean(BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_UPDATER)
    public RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater(
            @Qualifier(StoredSourcePlanRegistryWiringConfig.BEAN_STORED_SOURCE_PLAN_REGISTRY)
            StoredSourcePlanRegistry storedRegistry,
            @Qualifier(RuntimeSourcePlanRegistryWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_PUBLISHER)
            RuntimeSourcePlanRegistryPublisher runtimeRegistryPublisher
    ) {
        return new SnapshotRuntimeSourcePlanRegistryUpdater(
                storedRegistry,
                runtimeRegistryPublisher
        );
    }
}
