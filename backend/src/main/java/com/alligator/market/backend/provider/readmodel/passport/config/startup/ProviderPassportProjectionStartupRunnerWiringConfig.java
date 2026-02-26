package com.alligator.market.backend.provider.readmodel.passport.config.startup;

import com.alligator.market.backend.provider.readmodel.passport.startup.ProviderPassportProjectionStartupRunner;
import com.alligator.market.domain.provider.readmodel.passport.projection.ProviderPassportProjector;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Конфигурация wiring startup-runner для проекции паспортов провайдеров.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportProjectionStartupRunnerWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "providerPassportProjectionStartupRunner";

    /**
     * Startup-runner для синхронного построения проекции при старте приложения.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_STARTUP_RUNNER)
    @Order(Ordered.LOWEST_PRECEDENCE)
    @ConditionalOnBean({ProviderPassportProjector.class, TransactionTemplate.class})
    @ConditionalOnProperty(
            name = "provider.passport.projection.startup.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public ApplicationRunner providerPassportProjectionStartupRunner(
            ProviderPassportProjector projector,
            TransactionTemplate tx
    ) {
        return new ProviderPassportProjectionStartupRunner(projector, tx);
    }
}
