package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqProviderCodeExistenceAdapter;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link ProviderCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderCodeExistencePortWiringConfig {

    public static final String BEAN_PROVIDER_CODE_EXISTENCE_PORT = "providerCodeExistencePort";

    @Bean(BEAN_PROVIDER_CODE_EXISTENCE_PORT)
    public ProviderCodeExistencePort providerCodeExistencePort(DSLContext dsl) {
        return new JooqProviderCodeExistenceAdapter(dsl);
    }
}
