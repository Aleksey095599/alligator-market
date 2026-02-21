package com.alligator.market.backend.provider.maintenance.config.projection.db.passport.service;

import com.alligator.market.backend.provider.registry.wiring.ProviderRegistryWiringConfig;
import com.alligator.market.backend.provider.maintenance.config.projection.db.passport.dao.ProviderPassportDbProjectionDaoConfig;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import com.alligator.market.domain.provider.readmodel.store.passport.ProviderPassportReadModelStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring доменного сервиса {@link ProviderPassportDbProjection}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class,
        ProviderPassportDbProjectionDaoConfig.class
})
public class ProviderPassportDbProjectionConfig {

    public static final String BEAN_PROVIDER_PASSPORT_DB_PROJECTION = "providerPassportDbProjection";

    /**
     * Доменный use-case: обновление проекции паспортов провайдеров в БД по данным из контекста.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_DB_PROJECTION)
    public ProviderPassportDbProjection providerPassportDbProjection(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry,
            ProviderPassportReadModelStore repository,
            @Qualifier(ProviderPassportDbProjectionDaoConfig.BEAN_PROVIDER_PASSPORT_DB_PROJECTION_DAO)
            ProviderPassportDbProjectionDao projectionDao
    ) {
        return new ProviderPassportDbProjection(providerRegistry, repository, projectionDao);
    }
}
