package com.alligator.market.backend.marketdata.config.capturer.passport.application.projection.startup;

import com.alligator.market.backend.marketdata.capturer.passport.application.projection.MarketDataCapturerPassportProjectionService;
import com.alligator.market.backend.marketdata.capturer.passport.application.projection.runner.MarketDataCapturerPassportProjectionStartupRunner;
import com.alligator.market.backend.marketdata.config.capturer.passport.application.projection.service.MarketDataCapturerPassportProjectionServiceWiringConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MarketDataCapturerPassportProjectionStartupRunner}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataCapturerPassportProjectionServiceWiringConfig.class
})
public class MarketDataCapturerPassportProjectionStartupRunnerWiringConfig {

    public static final String BEAN_CAPTURER_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "capturerPassportProjectionStartupRunner";

    /**
     * Startup-runner для синхронного построения проекции при старте приложения.
     */
    @Bean(BEAN_CAPTURER_PASSPORT_PROJECTION_STARTUP_RUNNER)
    public ApplicationRunner capturerPassportProjectionStartupRunner(
            MarketDataCapturerPassportProjectionService service
    ) {
        return new MarketDataCapturerPassportProjectionStartupRunner(service);
    }
}
