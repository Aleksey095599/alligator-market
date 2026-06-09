package com.alligator.market.backend.source.config.handler.passport.store.sync;

import com.alligator.market.backend.source.config.handler.passport.persistence.store.SourceHandlerPassportStoreWiringConfig;
import com.alligator.market.backend.source.config.registry.RuntimeSourceRegistryWiringConfig;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportStore;
import com.alligator.market.domain.source.handler.passport.store.sync.SnapshotSourceHandlerPassportStoreSynchronizer;
import com.alligator.market.domain.source.handler.passport.store.sync.SourceHandlerPassportStoreSynchronizer;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeSourceRegistryWiringConfig.class,
        SourceHandlerPassportStoreWiringConfig.class
})
public class SourceHandlerPassportStoreSynchronizerWiringConfig {
    public static final String BEAN_SOURCE_HANDLER_PASSPORT_STORE_SYNCHRONIZER =
            "sourceHandlerPassportStoreSynchronizer";

    @Bean(BEAN_SOURCE_HANDLER_PASSPORT_STORE_SYNCHRONIZER)
    public SourceHandlerPassportStoreSynchronizer sourceHandlerPassportStoreSynchronizer(
            @Qualifier(RuntimeSourceRegistryWiringConfig.BEAN_RUNTIME_SOURCE_REGISTRY)
            RuntimeSourceRegistry sourceRegistry,
            @Qualifier(SourceHandlerPassportStoreWiringConfig.BEAN_SOURCE_HANDLER_PASSPORT_STORE)
            SourceHandlerPassportStore sourceHandlerPassportStore
    ) {
        return new SnapshotSourceHandlerPassportStoreSynchronizer(
                sourceRegistry,
                sourceHandlerPassportStore
        );
    }
}
