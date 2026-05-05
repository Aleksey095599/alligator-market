package com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.service;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.MDCaptureProcessPassportProjectionService;
import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MDCaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.marketdata.config.capture.process.passport.persistence.projection.port.adapter.MDCaptureProcessPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.registry.MDCaptureProcessRegistryWiringConfig;
import com.alligator.market.domain.marketdata.capture.process.registry.MDCaptureProcessRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wiring-конфигурация {@link MDCaptureProcessPassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MDCaptureProcessRegistryWiringConfig.class,
        MDCaptureProcessPassportProjectionWritePortWiringConfig.class
})
public class MDCaptureProcessPassportProjectionServiceWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_SERVICE =
            "captureProcessPassportProjectionService";

    /* Use case сервис проекции паспортов процессов захвата. */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_SERVICE)
    public MDCaptureProcessPassportProjectionService captureProcessPassportProjectionService(
            @Qualifier(MDCaptureProcessRegistryWiringConfig.BEAN_CAPTURE_PROCESS_REGISTRY)
            MDCaptureProcessRegistry registry,
            @Qualifier(MDCaptureProcessPassportProjectionWritePortWiringConfig
                    .BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_WRITE_PORT)
            MDCaptureProcessPassportProjectionWritePort writePort,
            PlatformTransactionManager txManager
    ) {
        return new MDCaptureProcessPassportProjectionService(
                registry,
                writePort,
                new TransactionTemplate(txManager)
        );
    }
}
