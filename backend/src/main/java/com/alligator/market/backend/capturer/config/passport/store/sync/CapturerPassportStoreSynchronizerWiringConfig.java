package com.alligator.market.backend.capturer.config.passport.store.sync;

import com.alligator.market.backend.capturer.config.passport.persistence.store.CapturerPassportStoreWiringConfig;
import com.alligator.market.backend.capturer.config.registry.CapturerRegistryWiringConfig;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportStore;
import com.alligator.market.domain.capturer.passport.store.sync.CapturerPassportStoreSynchronizer;
import com.alligator.market.domain.capturer.passport.store.sync.SnapshotCapturerPassportStoreSynchronizer;
import com.alligator.market.domain.capturer.registry.CapturerRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        CapturerRegistryWiringConfig.class,
        CapturerPassportStoreWiringConfig.class
})
public class CapturerPassportStoreSynchronizerWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZER =
            "capturerPassportStoreSynchronizer";

    @Bean(BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZER)
    public CapturerPassportStoreSynchronizer capturerPassportStoreSynchronizer(
            @Qualifier(CapturerRegistryWiringConfig.BEAN_CAPTURER_REGISTRY)
            CapturerRegistry capturerRegistry,
            @Qualifier(CapturerPassportStoreWiringConfig.BEAN_CAPTURER_PASSPORT_STORE)
            CapturerPassportStore capturerPassportStore
    ) {
        return new SnapshotCapturerPassportStoreSynchronizer(
                capturerRegistry,
                capturerPassportStore
        );
    }
}
