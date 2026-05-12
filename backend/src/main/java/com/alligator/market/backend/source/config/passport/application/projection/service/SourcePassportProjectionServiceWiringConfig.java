package com.alligator.market.backend.source.config.passport.application.projection.service;

import com.alligator.market.backend.source.config.passport.persistence.projection.port.adapter.SourcePassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.source.config.registry.SourceRegistryWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.SourcePassportProjectionService;
import com.alligator.market.backend.source.passport.application.projection.port.SourcePassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.registry.SourceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        SourceRegistryWiringConfig.class,
        SourcePassportProjectionWritePortWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class
})
public class SourcePassportProjectionServiceWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_PROJECTION_SERVICE =
            "sourcePassportProjectionService";

    @Bean(BEAN_SOURCE_PASSPORT_PROJECTION_SERVICE)
    public SourcePassportProjectionService sourcePassportProjectionService(
            @Qualifier(SourceRegistryWiringConfig.BEAN_SOURCE_REGISTRY)
            SourceRegistry sourceRegistry,
            @Qualifier(SourcePassportProjectionWritePortWiringConfig
                    .BEAN_SOURCE_PASSPORT_PROJECTION_WRITE_PORT)
            SourcePassportProjectionWritePort writePort,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new SourcePassportProjectionService(
                sourceRegistry,
                writePort,
                sourcePlanStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
