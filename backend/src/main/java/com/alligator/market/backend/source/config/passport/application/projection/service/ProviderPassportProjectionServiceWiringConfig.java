package com.alligator.market.backend.source.config.passport.application.projection.service;

import com.alligator.market.backend.source.passport.application.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.backend.source.config.passport.persistence.projection.port.adapter.ProviderPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.source.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.ProviderPassportProjectionService;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataSourceLifecycleStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.domain.source.registry.ProviderRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wiring-конфигурация {@link ProviderPassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class,
        ProviderPassportProjectionWritePortWiringConfig.class,
        MarketDataSourceLifecycleStatusSyncPortWiringConfig.class
})
public class ProviderPassportProjectionServiceWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_SERVICE =
            "providerPassportProjectionService";

    /* Use case сервис проекции паспортов провайдеров. */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_SERVICE)
    public ProviderPassportProjectionService providerPassportProjectionService(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry,
            @Qualifier(ProviderPassportProjectionWritePortWiringConfig.BEAN_PROVIDER_PASSPORT_PROJECTION_WRITE_PORT)
            ProviderPassportProjectionWritePort writePort,
            @Qualifier(MarketDataSourceLifecycleStatusSyncPortWiringConfig
                    .BEAN_MARKET_DATA_SOURCE_LIFECYCLE_STATUS_SYNC_PORT)
            MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new ProviderPassportProjectionService(
                providerRegistry,
                writePort,
                sourceLifecycleStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
