package com.alligator.market.backend.capturer.config.passport.persistence.registry;

import com.alligator.market.backend.capturer.passport.persistence.registry.JooqStoredCapturerPassportRegistryAdapter;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistry;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StoredCapturerPassportRegistryWiringConfig {
    public static final String BEAN_STORED_CAPTURER_PASSPORT_REGISTRY =
            "storedCapturerPassportRegistry";

    @Bean(BEAN_STORED_CAPTURER_PASSPORT_REGISTRY)
    public StoredCapturerPassportRegistry storedCapturerPassportRegistry(DSLContext dsl) {
        return new JooqStoredCapturerPassportRegistryAdapter(dsl);
    }
}
