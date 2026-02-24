package com.alligator.market.backend.provider.readmodel.passport.config.projection;

import com.alligator.market.domain.provider.readmodel.passport.projection.ProviderPassportProjector;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportProjectionWriteStore;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring доменного {@link ProviderPassportProjector}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportProjectorWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTOR = "providerPassportProjector";

    /**
     * Доменный projector паспортов провайдеров без дополнительного adapter-слоя.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTOR)
    public ProviderPassportProjector providerPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWriteStore writeStore
    ) {
        return new ProviderPassportProjector(providerRegistry, writeStore);
    }
}
