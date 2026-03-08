package com.alligator.market.backend.marketdata.provider.readmodel.passport.config.projection.startup;

import com.alligator.market.backend.marketdata.provider.readmodel.passport.config.projection.service.ProviderPassportProjectionServiceWiringConfig;
import com.alligator.market.backend.marketdata.provider.readmodel.passport.projection.service.ProviderPassportProjectionService;
import com.alligator.market.backend.marketdata.provider.readmodel.passport.projection.startup.ProviderPassportProjectionStartupRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring startup-runner для проекции паспортов провайдеров.
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
