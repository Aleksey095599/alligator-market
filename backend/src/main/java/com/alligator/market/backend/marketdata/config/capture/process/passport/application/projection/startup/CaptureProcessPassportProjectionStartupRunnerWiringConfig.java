package com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.startup;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.CaptureProcessPassportProjectionService;
import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.runner.CaptureProcessPassportProjectionStartupRunner;
import com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.service.CaptureProcessPassportProjectionServiceWiringConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link CaptureProcessPassportProjectionStartupRunner}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CaptureProcessPassportProjectionServiceWiringConfig.class
})
public class CaptureProcessPassportProjectionStartupRunnerWiringConfig {

    public static final String BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "captureProcessPassportProjectionStartupRunner";

    /**
     * Startup-runner для синхронного построения проекции при старте приложения.
     */
    @Bean(BEAN_CAPTURE_PROCESS_PASSPORT_PROJECTION_STARTUP_RUNNER)
    public ApplicationRunner captureProcessPassportProjectionStartupRunner(
            CaptureProcessPassportProjectionService service
    ) {
        return new CaptureProcessPassportProjectionStartupRunner(service);
    }
}
