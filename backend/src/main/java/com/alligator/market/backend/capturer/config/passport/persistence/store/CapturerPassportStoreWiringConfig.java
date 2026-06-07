package com.alligator.market.backend.capturer.config.passport.persistence.store;

import com.alligator.market.backend.capturer.passport.persistence.store.JooqCapturerPassportStoreAdapter;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportStore;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CapturerPassportStoreWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_STORE =
            "capturerPassportStore";

    @Bean(BEAN_CAPTURER_PASSPORT_STORE)
    public CapturerPassportStore capturerPassportStore(DSLContext dsl) {
        return new JooqCapturerPassportStoreAdapter(dsl);
    }
}
