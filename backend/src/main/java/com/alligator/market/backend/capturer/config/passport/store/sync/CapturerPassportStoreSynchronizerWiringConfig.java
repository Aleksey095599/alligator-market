package com.alligator.market.backend.capturer.config.passport.store.sync;

import com.alligator.market.backend.capturer.config.passport.persistence.store.CapturerPassportStoreWiringConfig;
import com.alligator.market.backend.capturer.config.passport.registry.CapturerPassportRegistryWiringConfig;
import com.alligator.market.domain.capturer.passport.registry.CapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportStore;
import com.alligator.market.domain.capturer.passport.store.sync.CapturerPassportStoreSynchronizer;
import com.alligator.market.domain.capturer.passport.store.sync.SnapshotCapturerPassportStoreSynchronizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        CapturerPassportRegistryWiringConfig.class,
        CapturerPassportStoreWiringConfig.class
})
public class CapturerPassportStoreSynchronizerWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZER =
            "capturerPassportStoreSynchronizer";

    @Bean(BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZER)
    public CapturerPassportStoreSynchronizer capturerPassportStoreSynchronizer(
            @Qualifier(CapturerPassportRegistryWiringConfig.BEAN_CAPTURER_PASSPORT_REGISTRY)
            CapturerPassportRegistry capturerPassportRegistry,
            @Qualifier(CapturerPassportStoreWiringConfig.BEAN_CAPTURER_PASSPORT_STORE)
            CapturerPassportStore capturerPassportStore
    ) {
        return new SnapshotCapturerPassportStoreSynchronizer(
                capturerPassportRegistry,
                capturerPassportStore
        );
    }
}
