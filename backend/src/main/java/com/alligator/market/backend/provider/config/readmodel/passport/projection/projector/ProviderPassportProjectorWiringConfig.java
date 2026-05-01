package com.alligator.market.backend.provider.config.readmodel.passport.projection.projector;

import com.alligator.market.backend.provider.config.readmodel.passport.projection.jooq.ProviderPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.backend.provider.application.passport.projection.ProviderPassportProjector;
import com.alligator.market.backend.provider.application.passport.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link ProviderPassportProjector}.
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
            ProviderPassportProjectionWritePort writePort
    ) {
        return new ProviderPassportProjector(providerRegistry, writePort);
    }
}
