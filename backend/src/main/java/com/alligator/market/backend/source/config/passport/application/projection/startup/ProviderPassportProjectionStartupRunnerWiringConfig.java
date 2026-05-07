package com.alligator.market.backend.source.config.passport.application.projection.startup;

import com.alligator.market.backend.source.config.passport.application.projection.service.ProviderPassportProjectionServiceWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.ProviderPassportProjectionService;
import com.alligator.market.backend.source.passport.application.projection.runner.ProviderPassportProjectionStartupRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link ProviderPassportProjectionStartupRunner}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderPassportProjectionServiceWiringConfig.class
})
public class ProviderPassportProjectionStartupRunnerWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "providerPassportProjectionStartupRunner";

    /**
     * Startup-runner для синхронного построения проекции при старте приложения.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_STARTUP_RUNNER)
    public ApplicationRunner providerPassportProjectionStartupRunner(
            ProviderPassportProjectionService service
    ) {
        return new ProviderPassportProjectionStartupRunner(service);
    }
}
