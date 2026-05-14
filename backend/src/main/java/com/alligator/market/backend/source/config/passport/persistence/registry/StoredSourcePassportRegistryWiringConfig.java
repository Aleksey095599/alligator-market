package com.alligator.market.backend.source.config.passport.persistence.registry;

import com.alligator.market.backend.source.passport.persistence.registry.JooqStoredSourcePassportRegistryAdapter;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StoredSourcePassportRegistryWiringConfig {
    public static final String BEAN_STORED_SOURCE_PASSPORT_REGISTRY =
            "storedSourcePassportRegistry";

    @Bean(BEAN_STORED_SOURCE_PASSPORT_REGISTRY)
    public StoredSourcePassportRegistry storedSourcePassportRegistry(DSLContext dsl) {
        return new JooqStoredSourcePassportRegistryAdapter(dsl);
    }
}
