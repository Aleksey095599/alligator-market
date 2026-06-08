package com.alligator.market.backend.source.config.passport.persistence.store;

import com.alligator.market.backend.source.passport.persistence.store.JooqSourcePassportStoreAdapter;
import com.alligator.market.domain.source.passport.store.SourcePassportStore;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourcePassportStoreWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_STORE =
            "sourcePassportStore";

    @Bean(BEAN_SOURCE_PASSPORT_STORE)
    public SourcePassportStore sourcePassportStore(DSLContext dsl) {
        return new JooqSourcePassportStoreAdapter(dsl);
    }
}
