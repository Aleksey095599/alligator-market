package com.alligator.market.backend.provider.config.readmodel.passport.projection.service;

import com.alligator.market.backend.provider.application.passport.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.backend.provider.config.readmodel.passport.projection.jooq.ProviderPassportProjectionWritePortWiringConfig;
import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.backend.provider.readmodel.passport.projection.service.ProviderPassportProjectionService;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wiring-конфигурация {@link ProviderPassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class,
        ProviderPassportProjectionWritePortWiringConfig.class
})
public class ProviderPassportProjectionServiceWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_SERVICE =
            "providerPassportProjectionService";

    /* Use case сервис проекции паспортов провайдеров. */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_SERVICE)
    public ProviderPassportProjectionService providerPassportProjectionService(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry,
            @Qualifier(ProviderPassportProjectionWritePortWiringConfig.BEAN_PROVIDER_PASSPORT_PROJECTION_WRITE_PORT)
            ProviderPassportProjectionWritePort writePort,
            PlatformTransactionManager txManager
    ) {
        return new ProviderPassportProjectionService(
                providerRegistry,
                writePort,
                new TransactionTemplate(txManager)
        );
    }
}
