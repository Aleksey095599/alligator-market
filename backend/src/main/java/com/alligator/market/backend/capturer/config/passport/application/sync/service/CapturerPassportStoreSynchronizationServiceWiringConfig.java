package com.alligator.market.backend.capturer.config.passport.application.sync.service;

import com.alligator.market.backend.capturer.config.passport.store.sync.CapturerPassportStoreSynchronizerWiringConfig;
import com.alligator.market.backend.capturer.passport.application.sync.CapturerPassportStoreSynchronizationService;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.sync.RuntimeSourcePlanRegistryUpdaterWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.passport.store.sync.CapturerPassportStoreSynchronizer;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        CapturerPassportStoreSynchronizerWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class,
        RuntimeSourcePlanRegistryUpdaterWiringConfig.class
})
public class CapturerPassportStoreSynchronizationServiceWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZATION_SERVICE =
            "capturerPassportStoreSynchronizationService";

    @Bean(BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZATION_SERVICE)
    public CapturerPassportStoreSynchronizationService capturerPassportStoreSynchronizationService(
            @Qualifier(CapturerPassportStoreSynchronizerWiringConfig
                    .BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZER)
            CapturerPassportStoreSynchronizer capturerPassportStoreSynchronizer,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            @Qualifier(RuntimeSourcePlanRegistryUpdaterWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_UPDATER)
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            PlatformTransactionManager txManager
    ) {
        return new CapturerPassportStoreSynchronizationService(
                capturerPassportStoreSynchronizer,
                sourcePlanStatusSyncPort,
                runtimeSourcePlanRegistryUpdater,
                new TransactionTemplate(txManager)
        );
    }
}
