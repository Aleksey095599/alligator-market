package com.alligator.market.backend.sourcing.config.plan.application.query.options.adapter;

import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.query.options.adapter.JooqInstrumentOptionsQueryAdapter;
import com.alligator.market.backend.sourcing.plan.application.query.options.adapter.RegistryProviderOptionsQueryAdapter;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.ProviderOptionsQueryPort;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link JooqInstrumentOptionsQueryAdapter}.
 */
@Configuration(proxyBeanMethods = false)
@Import(ProviderRegistryWiringConfig.class)
public class InstrumentSourcePlanOptionsQueryWiringConfig {

    public static final String BEAN_INSTRUMENT_OPTIONS_QUERY_PORT = "instrumentOptionsQueryPort";
    public static final String BEAN_PROVIDER_OPTIONS_QUERY_PORT = "providerOptionsQueryPort";

    @Bean(BEAN_INSTRUMENT_OPTIONS_QUERY_PORT)
    public InstrumentOptionsQueryPort instrumentOptionsQueryPort(DSLContext dsl) {
        return new JooqInstrumentOptionsQueryAdapter(dsl);
    }

    @Bean(BEAN_PROVIDER_OPTIONS_QUERY_PORT)
    public ProviderOptionsQueryPort providerOptionsQueryPort(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry
    ) {
        return new RegistryProviderOptionsQueryAdapter(providerRegistry);
    }
}
