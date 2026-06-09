package com.alligator.market.backend.source.config.passport.application.sync.service;

import com.alligator.market.backend.source.config.handler.passport.store.sync.SourceHandlerPassportStoreSynchronizerWiringConfig;
import com.alligator.market.backend.source.config.passport.store.sync.SourcePassportStoreSynchronizerWiringConfig;
import com.alligator.market.backend.source.passport.application.sync.SourcePassportStoreSynchronizationService;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.sync.RuntimeSourcePlanRegistryUpdaterWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.handler.passport.store.sync.SourceHandlerPassportStoreSynchronizer;
import com.alligator.market.domain.source.passport.store.sync.SourcePassportStoreSynchronizer;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePassportStoreSynchronizerWiringConfig.class,
        SourceHandlerPassportStoreSynchronizerWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class,
        RuntimeSourcePlanRegistryUpdaterWiringConfig.class
})
public class SourcePassportStoreSynchronizationServiceWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZATION_SERVICE =
            "sourcePassportStoreSynchronizationService";

    @Bean(BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZATION_SERVICE)
    public SourcePassportStoreSynchronizationService sourcePassportStoreSynchronizationService(
            @Qualifier(SourcePassportStoreSynchronizerWiringConfig.BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZER)
            SourcePassportStoreSynchronizer sourcePassportStoreSynchronizer,
            @Qualifier(SourceHandlerPassportStoreSynchronizerWiringConfig
                    .BEAN_SOURCE_HANDLER_PASSPORT_STORE_SYNCHRONIZER)
            SourceHandlerPassportStoreSynchronizer sourceHandlerPassportStoreSynchronizer,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            @Qualifier(RuntimeSourcePlanRegistryUpdaterWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_UPDATER)
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            PlatformTransactionManager txManager
    ) {
        return new SourcePassportStoreSynchronizationService(
                sourcePassportStoreSynchronizer,
                sourceHandlerPassportStoreSynchronizer,
                sourcePlanStatusSyncPort,
                runtimeSourcePlanRegistryUpdater,
                new TransactionTemplate(txManager)
        );
    }
}
