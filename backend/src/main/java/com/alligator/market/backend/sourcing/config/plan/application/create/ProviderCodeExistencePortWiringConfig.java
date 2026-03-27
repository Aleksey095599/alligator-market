package com.alligator.market.backend.sourcing.config.plan.application.create;

import com.alligator.market.backend.sourcing.plan.application.create.adapter.JooqProviderCodeExistenceAdapter;
import com.alligator.market.backend.sourcing.plan.application.create.port.ProviderCodeExistencePort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring {@link ProviderCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderCodeExistencePortWiringConfig {

    public static final String BEAN_PROVIDER_CODE_EXISTENCE_PORT = "providerCodeExistencePort";

    /**
     * Порт проверки существования провайдера по коду.
     */
    @Bean(BEAN_PROVIDER_CODE_EXISTENCE_PORT)
    public ProviderCodeExistencePort providerCodeExistencePort(DSLContext dsl) {
        return new JooqProviderCodeExistenceAdapter(dsl);
    }
}
