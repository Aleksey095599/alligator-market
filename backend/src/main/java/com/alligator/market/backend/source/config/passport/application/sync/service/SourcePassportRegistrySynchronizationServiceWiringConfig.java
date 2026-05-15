package com.alligator.market.backend.source.config.passport.application.sync.service;

import com.alligator.market.backend.source.config.passport.registry.sync.StoredSourcePassportRegistryUpdaterWiringConfig;
import com.alligator.market.backend.source.passport.application.sync.SourcePassportRegistrySynchronizationService;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.sync.RuntimeSourcePlanRegistryUpdaterWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.passport.registry.sync.StoredSourcePassportRegistryUpdater;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        StoredSourcePassportRegistryUpdaterWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class,
        RuntimeSourcePlanRegistryUpdaterWiringConfig.class
})
public class SourcePassportRegistrySynchronizationServiceWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_REGISTRY_SYNCHRONIZATION_SERVICE =
            "sourcePassportRegistrySynchronizationService";

    @Bean(BEAN_SOURCE_PASSPORT_REGISTRY_SYNCHRONIZATION_SERVICE)
    public SourcePassportRegistrySynchronizationService sourcePassportRegistrySynchronizationService(
            @Qualifier(StoredSourcePassportRegistryUpdaterWiringConfig.BEAN_STORED_SOURCE_PASSPORT_REGISTRY_UPDATER)
            StoredSourcePassportRegistryUpdater storedSourcePassportRegistryUpdater,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            @Qualifier(RuntimeSourcePlanRegistryUpdaterWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_UPDATER)
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            PlatformTransactionManager txManager
    ) {
        return new SourcePassportRegistrySynchronizationService(
                storedSourcePassportRegistryUpdater,
                sourcePlanStatusSyncPort,
                runtimeSourcePlanRegistryUpdater,
                new TransactionTemplate(txManager)
        );
    }
}
