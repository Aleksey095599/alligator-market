package com.alligator.market.backend.provider.readmodel.passport.config.projection.service;

import com.alligator.market.backend.provider.readmodel.passport.config.projection.ProviderPassportProjectorWiringConfig;
import com.alligator.market.backend.provider.readmodel.passport.projection.service.ProviderPassportProjectionService;
import com.alligator.market.domain.provider.readmodel.passport.projection.ProviderPassportProjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Конфигурация wiring {@link ProviderPassportProjectionService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderPassportProjectorWiringConfig.class
})
public class ProviderPassportProjectionServiceWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_SERVICE =
            "providerPassportProjectionService";

    /* Use case сервис проекции паспортов провайдеров. */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_SERVICE)
    public ProviderPassportProjectionService providerPassportProjectionService(
            ProviderPassportProjector projector,
            PlatformTransactionManager txManager
    ) {
        return new ProviderPassportProjectionService(projector, new TransactionTemplate(txManager));
    }
}
