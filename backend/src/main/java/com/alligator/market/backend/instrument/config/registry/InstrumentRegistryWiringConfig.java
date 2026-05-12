package com.alligator.market.backend.instrument.config.registry;

import com.alligator.market.backend.instrument.registry.persistence.jooq.JooqInstrumentRegistryAdapter;
import com.alligator.market.domain.instrument.registry.InstrumentRegistry;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class InstrumentRegistryWiringConfig {
    public static final String BEAN_INSTRUMENT_REGISTRY = "instrumentRegistry";

    @Bean(BEAN_INSTRUMENT_REGISTRY)
    public InstrumentRegistry instrumentRegistry(DSLContext dsl) {
        return new JooqInstrumentRegistryAdapter(dsl);
    }
}
