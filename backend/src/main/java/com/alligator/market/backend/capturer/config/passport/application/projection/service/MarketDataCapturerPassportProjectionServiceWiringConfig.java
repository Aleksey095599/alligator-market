package com.alligator.market.backend.capturer.config.passport.application.projection.service;

import com.alligator.market.backend.capturer.passport.application.projection.MarketDataCapturerPassportProjectionService;
import com.alligator.market.backend.capturer.config.passport.persistence.registry.StoredCapturerPassportRegistryWiringConfig;
import com.alligator.market.backend.capturer.config.passport.registry.RuntimeCapturerPassportRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.passport.registry.runtime.RuntimeCapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeCapturerPassportRegistryWiringConfig.class,
        StoredCapturerPassportRegistryWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class
})
public class MarketDataCapturerPassportProjectionServiceWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_PROJECTION_SERVICE =
            "capturerPassportProjectionService";

    @Bean(BEAN_CAPTURER_PASSPORT_PROJECTION_SERVICE)
    public MarketDataCapturerPassportProjectionService capturerPassportProjectionService(
            @Qualifier(RuntimeCapturerPassportRegistryWiringConfig.BEAN_RUNTIME_CAPTURER_PASSPORT_REGISTRY)
            RuntimeCapturerPassportRegistry runtimePassportRegistry,
            @Qualifier(StoredCapturerPassportRegistryWiringConfig.BEAN_STORED_CAPTURER_PASSPORT_REGISTRY)
            StoredCapturerPassportRegistry storedPassportRegistry,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new MarketDataCapturerPassportProjectionService(
                runtimePassportRegistry,
                storedPassportRegistry,
                sourcePlanStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
