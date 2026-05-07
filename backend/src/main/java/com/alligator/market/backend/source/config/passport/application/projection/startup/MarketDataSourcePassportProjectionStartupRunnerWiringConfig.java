package com.alligator.market.backend.source.config.passport.application.projection.startup;

import com.alligator.market.backend.source.config.passport.application.projection.service.MarketDataSourcePassportProjectionServiceWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.MarketDataSourcePassportProjectionService;
import com.alligator.market.backend.source.passport.application.projection.runner.MarketDataSourcePassportProjectionStartupRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MarketDataSourcePassportProjectionStartupRunner}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePassportProjectionServiceWiringConfig.class
})
public class MarketDataSourcePassportProjectionStartupRunnerWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "marketDataSourcePassportProjectionStartupRunner";

    /**
     * Startup-runner для синхронного построения проекции при старте приложения.
     */
    @Bean(BEAN_MARKET_DATA_SOURCE_PASSPORT_PROJECTION_STARTUP_RUNNER)
    public ApplicationRunner marketDataSourcePassportProjectionStartupRunner(
            MarketDataSourcePassportProjectionService service
    ) {
        return new MarketDataSourcePassportProjectionStartupRunner(service);
    }
}
