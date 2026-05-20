package com.alligator.market.backend.instrument.config.registry;

import com.alligator.market.backend.instrument.registry.persistence.jooq.JooqStoredInstrumentRegistryAdapter;
import com.alligator.market.domain.instrument.registry.stored.StoredInstrumentRegistry;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StoredInstrumentRegistryWiringConfig {
    public static final String BEAN_STORED_INSTRUMENT_REGISTRY = "storedInstrumentRegistry";

    @Bean(BEAN_STORED_INSTRUMENT_REGISTRY)
    public StoredInstrumentRegistry storedInstrumentRegistry(DSLContext dsl) {
        return new JooqStoredInstrumentRegistryAdapter(dsl);
    }
}
