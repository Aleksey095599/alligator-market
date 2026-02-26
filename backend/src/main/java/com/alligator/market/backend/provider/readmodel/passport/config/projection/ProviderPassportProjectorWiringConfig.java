package com.alligator.market.backend.provider.readmodel.passport.config.projection;

import com.alligator.market.backend.provider.readmodel.passport.config.store.ProviderPassportProjectionWriteStoreWiringConfig;
import com.alligator.market.backend.provider.registry.wiring.ProviderRegistryWiringConfig;
import com.alligator.market.domain.provider.readmodel.passport.projection.ProviderPassportProjector;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportProjectionWriteStore;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring {@link ProviderPassportProjector}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class,
        ProviderPassportProjectionWriteStoreWiringConfig.class,
})
public class ProviderPassportProjectorWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTOR = "providerPassportProjector";

    /**
     * Проектор паспортов провайдеров.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTOR)
    public ProviderPassportProjector providerPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWriteStore writeStore
    ) {
        return new ProviderPassportProjector(providerRegistry, writeStore);
    }
}
