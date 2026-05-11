package com.alligator.market.backend.capturer.config.passport.application.projection.service;

import com.alligator.market.backend.capturer.passport.application.projection.MarketDataCapturerPassportProjectionService;
import com.alligator.market.backend.capturer.passport.application.projection.port.MarketDataCapturerPassportProjectionWritePort;
import com.alligator.market.backend.capturer.config.passport.persistence.projection.port.adapter.MarketDataCapturerPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.capturer.config.registry.MarketDataCapturerRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourcePlanStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.registry.MarketDataCapturerRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataCapturerRegistryWiringConfig.class,
        MarketDataCapturerPassportProjectionWritePortWiringConfig.class,
        SourcePlanStatusSyncPortWiringConfig.class
})
public class MarketDataCapturerPassportProjectionServiceWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_PROJECTION_SERVICE =
            "capturerPassportProjectionService";

    @Bean(BEAN_CAPTURER_PASSPORT_PROJECTION_SERVICE)
    public MarketDataCapturerPassportProjectionService capturerPassportProjectionService(
            @Qualifier(MarketDataCapturerRegistryWiringConfig.BEAN_CAPTURER_REGISTRY)
            MarketDataCapturerRegistry registry,
            @Qualifier(MarketDataCapturerPassportProjectionWritePortWiringConfig
                    .BEAN_CAPTURER_PASSPORT_PROJECTION_WRITE_PORT)
            MarketDataCapturerPassportProjectionWritePort writePort,
            @Qualifier(SourcePlanStatusSyncPortWiringConfig.BEAN_SOURCE_PLAN_STATUS_SYNC_PORT)
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new MarketDataCapturerPassportProjectionService(
                registry,
                writePort,
                sourcePlanStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
