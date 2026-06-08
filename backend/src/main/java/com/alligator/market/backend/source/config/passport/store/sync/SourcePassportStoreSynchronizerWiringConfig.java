package com.alligator.market.backend.source.config.passport.store.sync;

import com.alligator.market.backend.source.config.passport.persistence.store.SourcePassportStoreWiringConfig;
import com.alligator.market.backend.source.config.registry.RuntimeSourceRegistryWiringConfig;
import com.alligator.market.domain.source.passport.store.SourcePassportStore;
import com.alligator.market.domain.source.passport.store.sync.SnapshotSourcePassportStoreSynchronizer;
import com.alligator.market.domain.source.passport.store.sync.SourcePassportStoreSynchronizer;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeSourceRegistryWiringConfig.class,
        SourcePassportStoreWiringConfig.class
})
public class SourcePassportStoreSynchronizerWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZER =
            "sourcePassportStoreSynchronizer";

    @Bean(BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZER)
    public SourcePassportStoreSynchronizer sourcePassportStoreSynchronizer(
            @Qualifier(RuntimeSourceRegistryWiringConfig.BEAN_RUNTIME_SOURCE_REGISTRY)
            RuntimeSourceRegistry sourceRegistry,
            @Qualifier(SourcePassportStoreWiringConfig.BEAN_SOURCE_PASSPORT_STORE)
            SourcePassportStore sourcePassportStore
    ) {
        return new SnapshotSourcePassportStoreSynchronizer(
                sourceRegistry,
                sourcePassportStore
        );
    }
}
