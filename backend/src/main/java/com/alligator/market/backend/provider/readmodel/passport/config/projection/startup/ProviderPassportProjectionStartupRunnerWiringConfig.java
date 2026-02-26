package com.alligator.market.backend.provider.readmodel.passport.config.projection.startup;

import com.alligator.market.backend.provider.readmodel.passport.config.projection.ProviderPassportProjectionServiceWiringConfig;
import com.alligator.market.backend.provider.readmodel.passport.projection.service.ProviderPassportProjectionService;
import com.alligator.market.backend.provider.readmodel.passport.projection.startup.ProviderPassportProjectionStartupRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

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
    @Order(Ordered.LOWEST_PRECEDENCE)
    @ConditionalOnBean(ProviderPassportProjectionService.class)
    @ConditionalOnProperty(
            name = "provider.passport.projection.startup.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public ApplicationRunner providerPassportProjectionStartupRunner(
            ProviderPassportProjectionService service
    ) {
        return new ProviderPassportProjectionStartupRunner(service);
    }
}
