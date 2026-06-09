package com.alligator.market.backend.source.config.handler.passport.persistence.store;

import com.alligator.market.backend.source.handler.passport.persistence.store.JooqSourceHandlerPassportStoreAdapter;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportStore;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourceHandlerPassportStoreWiringConfig {
    public static final String BEAN_SOURCE_HANDLER_PASSPORT_STORE =
            "sourceHandlerPassportStore";

    @Bean(BEAN_SOURCE_HANDLER_PASSPORT_STORE)
    public SourceHandlerPassportStore sourceHandlerPassportStore(DSLContext dsl) {
        return new JooqSourceHandlerPassportStoreAdapter(dsl);
    }
}
