package com.alligator.market.backend.provider.readmodel.passport.config.projection.projector;

import com.alligator.market.backend.provider.readmodel.passport.config.projection.jdbc.ProviderPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.provider.registry.config.ProviderRegistryWiringConfig;
import com.alligator.market.domain.provider.readmodel.passport.projection.projector.ProviderPassportProjector;
import com.alligator.market.domain.provider.readmodel.passport.projection.port.ProviderPassportProjectionWritePort;
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
        ProviderPassportProjectionWritePortWiringConfig.class,
})
public class ProviderPassportProjectorWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTOR = "providerPassportProjector";

    /**
     * Проектор паспортов провайдеров.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTOR)
    public ProviderPassportProjector providerPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWritePort writeStore
    ) {
        return new ProviderPassportProjector(providerRegistry, writeStore);
    }
}
