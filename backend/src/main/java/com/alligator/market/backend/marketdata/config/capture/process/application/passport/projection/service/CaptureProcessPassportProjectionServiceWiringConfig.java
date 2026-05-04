package com.alligator.market.backend.marketdata.config.capture.process.application.passport.projection.service;

import com.alligator.market.backend.marketdata.capture.process.application.passport.projection.CaptureProcessPassportProjectionService;
import com.alligator.market.backend.marketdata.capture.process.application.passport.projection.port.CaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.config.capture.process.persistence.passport.projection.port.adapter.CaptureProcessPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.registry.CaptureProcessRegistryWiringConfig;
import com.alligator.market.domain.marketdata.capture.process.registry.CaptureProcessRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wiring-конфигурация {@link CaptureProcessPassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CaptureProcessRegistryWiringConfig.class,
        CaptureProcessPassportProjectionWritePortWiringConfig.class
})
public class CaptureProcessPassportProjectionServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_SERVICE =
            "captureProcessPassportProjectionService";

    /* Use case сервис проекции паспортов процессов фиксации. */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_SERVICE)
    public CaptureProcessPassportProjectionService captureProcessPassportProjectionService(
            @Qualifier(CaptureProcessRegistryWiringConfig.BEAN_CAPTURE_PROCESS_REGISTRY)
            CaptureProcessRegistry registry,
            @Qualifier(CaptureProcessPassportProjectionWritePortWiringConfig
                    .BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT)
            CaptureProcessPassportProjectionWritePort writePort,
            PlatformTransactionManager txManager
    ) {
        return new CaptureProcessPassportProjectionService(
                registry,
                writePort,
                new TransactionTemplate(txManager)
        );
    }
}
