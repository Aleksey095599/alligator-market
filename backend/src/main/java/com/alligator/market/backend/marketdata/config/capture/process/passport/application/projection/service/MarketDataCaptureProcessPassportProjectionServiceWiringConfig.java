package com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.service;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.MarketDataCaptureProcessPassportProjectionService;
import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MarketDataCaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.config.capture.process.passport.persistence.projection.port.adapter.MarketDataCaptureProcessPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.registry.MarketDataCaptureProcessRegistryWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.MarketDataSourceLifecycleStatusSyncPortWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.domain.marketdata.capture.process.registry.MarketDataCaptureProcessRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wiring-конфигурация {@link MarketDataCaptureProcessPassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataCaptureProcessRegistryWiringConfig.class,
        MarketDataCaptureProcessPassportProjectionWritePortWiringConfig.class,
        MarketDataSourceLifecycleStatusSyncPortWiringConfig.class
})
public class MarketDataCaptureProcessPassportProjectionServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_SERVICE =
            "captureProcessPassportProjectionService";

    /* Use case сервис проекции паспортов процессов захвата. */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_SERVICE)
    public MarketDataCaptureProcessPassportProjectionService captureProcessPassportProjectionService(
            @Qualifier(MarketDataCaptureProcessRegistryWiringConfig.BEAN_CAPTURE_PROCESS_REGISTRY)
            MarketDataCaptureProcessRegistry registry,
            @Qualifier(MarketDataCaptureProcessPassportProjectionWritePortWiringConfig
                    .BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT)
            MarketDataCaptureProcessPassportProjectionWritePort writePort,
            @Qualifier(MarketDataSourceLifecycleStatusSyncPortWiringConfig
                    .BEAN_MARKET_DATA_SOURCE_LIFECYCLE_STATUS_SYNC_PORT)
            MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort,
            PlatformTransactionManager txManager
    ) {
        return new MarketDataCaptureProcessPassportProjectionService(
                registry,
                writePort,
                sourceLifecycleStatusSyncPort,
                new TransactionTemplate(txManager)
        );
    }
}
