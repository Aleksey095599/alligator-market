package com.alligator.market.backend.instrument.config.identity;

import com.alligator.market.backend.instrument.identity.persistence.jooq.JooqInstrumentIdentityStoreAdapter;
import com.alligator.market.domain.instrument.identity.InstrumentIdentityStore;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class InstrumentIdentityStoreWiringConfig {
    public static final String BEAN_INSTRUMENT_IDENTITY_STORE = "instrumentIdentityStore";

    @Bean(BEAN_INSTRUMENT_IDENTITY_STORE)
    public InstrumentIdentityStore instrumentIdentityStore(DSLContext dsl) {
        return new JooqInstrumentIdentityStoreAdapter(dsl);
    }
}
