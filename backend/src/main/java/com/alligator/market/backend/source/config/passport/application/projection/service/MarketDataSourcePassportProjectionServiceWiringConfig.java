package com.alligator.market.backend.source.config.passport.application.projection.service;

import com.alligator.market.backend.source.config.passport.persistence.projection.port.adapter.MarketDataSourcePassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.source.config.registry.MarketDataSourceRegistryWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.MarketDataSourcePassportProjectionService;
import com.alligator.market.backend.source.passport.application.projection.port.MarketDataSourcePassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataSourceLifecycleStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wiring-конфигурация {@link MarketDataSourcePassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourceRegistryWiringConfig.class,
        MarketDataSourcePassportProjectionWritePortWiringConfig.class,
        MarketDataSourceLifecycleStatusSyncPortWiringConfig.class
})
public class MarketDataSourcePassportProjectionServiceWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_SERVICE =
            "marketDataSourcePassportProjectionService";

    /* Use case сервис проекции паспортов провайдеров. */
    @Bean(BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_SERVICE)
    public MarketDataSourcePassportProjectionService marketDataSourcePassportProjectionService(
            @Qualifier(MarketDataSourceRegistryWiringConfig.BEAN_MARKET_DATA_SOURCE_REGISTRY)
            MarketDataSourceRegistry sourceRegistry,
            @Qualifier(MarketDataSourcePassportProjectionWritePortWiringConfig
                    .BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_WRITE_PORT)
            MarketDataSourcePassportProjectionWritePort writePort,
            @Qualifier(MarketDataSourceLifecycleStatusSyncPortWiringConfig
                    .BEAN_MARKET_DATA_SOURCE_LIFECYCLE_STATUS_SYNC_PORT)
            MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new MarketDataSourcePassportProjectionService(
                sourceRegistry,
                writePort,
                sourceLifecycleStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
