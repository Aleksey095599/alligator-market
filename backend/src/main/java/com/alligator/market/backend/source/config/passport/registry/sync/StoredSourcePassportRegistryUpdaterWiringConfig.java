package com.alligator.market.backend.source.config.passport.registry.sync;

import com.alligator.market.backend.source.config.passport.persistence.registry.StoredSourcePassportRegistryWiringConfig;
import com.alligator.market.backend.source.config.passport.registry.RuntimeSourcePassportRegistryWiringConfig;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.sync.SnapshotStoredSourcePassportRegistryUpdater;
import com.alligator.market.domain.source.passport.registry.sync.StoredSourcePassportRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeSourcePassportRegistryWiringConfig.class,
        StoredSourcePassportRegistryWiringConfig.class
})
public class StoredSourcePassportRegistryUpdaterWiringConfig {
    public static final String BEAN_STORED_SOURCE_PASSPORT_REGISTRY_UPDATER =
            "storedSourcePassportRegistryUpdater";

    @Bean(BEAN_STORED_SOURCE_PASSPORT_REGISTRY_UPDATER)
    public StoredSourcePassportRegistryUpdater storedSourcePassportRegistryUpdater(
            @Qualifier(RuntimeSourcePassportRegistryWiringConfig.BEAN_RUNTIME_SOURCE_PASSPORT_REGISTRY)
            RuntimeSourcePassportRegistry runtimeRegistry,
            @Qualifier(StoredSourcePassportRegistryWiringConfig.BEAN_STORED_SOURCE_PASSPORT_REGISTRY)
            StoredSourcePassportRegistry storedRegistry
    ) {
        return new SnapshotStoredSourcePassportRegistryUpdater(
                runtimeRegistry,
                storedRegistry
        );
    }
}
