package com.alligator.market.backend.capturer.config.passport.registry.sync;

import com.alligator.market.backend.capturer.config.passport.persistence.registry.StoredCapturerPassportRegistryWiringConfig;
import com.alligator.market.backend.capturer.config.passport.registry.RuntimeCapturerPassportRegistryWiringConfig;
import com.alligator.market.domain.capturer.passport.registry.runtime.RuntimeCapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.registry.sync.SnapshotStoredCapturerPassportRegistryUpdater;
import com.alligator.market.domain.capturer.passport.registry.sync.StoredCapturerPassportRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeCapturerPassportRegistryWiringConfig.class,
        StoredCapturerPassportRegistryWiringConfig.class
})
public class StoredCapturerPassportRegistryUpdaterWiringConfig {
    public static final String BEAN_STORED_CAPTURER_PASSPORT_REGISTRY_UPDATER =
            "storedCapturerPassportRegistryUpdater";

    @Bean(BEAN_STORED_CAPTURER_PASSPORT_REGISTRY_UPDATER)
    public StoredCapturerPassportRegistryUpdater storedCapturerPassportRegistryUpdater(
            @Qualifier(RuntimeCapturerPassportRegistryWiringConfig.BEAN_RUNTIME_CAPTURER_PASSPORT_REGISTRY)
            RuntimeCapturerPassportRegistry runtimeRegistry,
            @Qualifier(StoredCapturerPassportRegistryWiringConfig.BEAN_STORED_CAPTURER_PASSPORT_REGISTRY)
            StoredCapturerPassportRegistry storedRegistry
    ) {
        return new SnapshotStoredCapturerPassportRegistryUpdater(
                runtimeRegistry,
                storedRegistry
        );
    }
}
