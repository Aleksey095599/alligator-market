package com.alligator.market.backend.provider.config.application.passport.catalog;

import com.alligator.market.backend.provider.application.passport.catalog.PassportCatalogService;
import com.alligator.market.backend.provider.application.passport.catalog.PassportCatalogServiceImpl;
import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link PassportCatalogService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class
})
public class PassportCatalogServiceWiringConfig {

    public static final String BEAN_PASSPORT_CATALOG_SERVICE = "passportCatalogService";

    /**
     * Сервис каталога паспортов провайдеров.
     */
    @Bean(BEAN_PASSPORT_CATALOG_SERVICE)
    public PassportCatalogService passportCatalogService(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry
    ) {
        return new PassportCatalogServiceImpl(providerRegistry);
    }
}
