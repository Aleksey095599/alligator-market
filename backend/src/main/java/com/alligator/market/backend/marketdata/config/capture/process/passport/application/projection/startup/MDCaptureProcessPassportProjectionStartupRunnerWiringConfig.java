package com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.startup;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.MDCaptureProcessPassportProjectionService;
import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.runner.MDCaptureProcessPassportProjectionStartupRunner;
import com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.service.MDCaptureProcessPassportProjectionServiceWiringConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MDCaptureProcessPassportProjectionStartupRunner}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MDCaptureProcessPassportProjectionServiceWiringConfig.class
})
public class MDCaptureProcessPassportProjectionStartupRunnerWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "captureProcessPassportProjectionStartupRunner";

    /**
     * Startup-runner для синхронного построения проекции при старте приложения.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_STARTUP_RUNNER)
    public ApplicationRunner captureProcessPassportProjectionStartupRunner(
            MDCaptureProcessPassportProjectionService service
    ) {
        return new MDCaptureProcessPassportProjectionStartupRunner(service);
    }
}
