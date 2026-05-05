package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqProviderExistenceAdapter;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderExistencePort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link ProviderExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderExistencePortWiringConfig {

    public static final String BEAN_PROVIDER_EXISTENCE_PORT = "providerExistencePort";

    @Bean(BEAN_PROVIDER_EXISTENCE_PORT)
    public ProviderExistencePort providerExistencePort(DSLContext dsl) {
        return new JooqProviderExistenceAdapter(dsl);
    }
}
