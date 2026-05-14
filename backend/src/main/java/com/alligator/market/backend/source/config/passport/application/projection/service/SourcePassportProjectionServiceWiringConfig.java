package com.alligator.market.backend.source.config.passport.application.projection.service;

import com.alligator.market.backend.source.config.passport.persistence.registry.StoredSourcePassportRegistryWiringConfig;
import com.alligator.market.backend.source.config.passport.registry.RuntimeSourcePassportRegistryWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.SourcePassportProjectionService;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeSourcePassportRegistryWiringConfig.class,
        StoredSourcePassportRegistryWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class
})
public class SourcePassportProjectionServiceWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_PROJECTION_SERVICE =
            "sourcePassportProjectionService";

    @Bean(BEAN_SOURCE_PASSPORT_PROJECTION_SERVICE)
    public SourcePassportProjectionService sourcePassportProjectionService(
            @Qualifier(RuntimeSourcePassportRegistryWiringConfig.BEAN_RUNTIME_SOURCE_PASSPORT_REGISTRY)
            RuntimeSourcePassportRegistry runtimePassportRegistry,
            @Qualifier(StoredSourcePassportRegistryWiringConfig.BEAN_STORED_SOURCE_PASSPORT_REGISTRY)
            StoredSourcePassportRegistry storedPassportRegistry,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new SourcePassportProjectionService(
                runtimePassportRegistry,
                storedPassportRegistry,
                sourcePlanStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
