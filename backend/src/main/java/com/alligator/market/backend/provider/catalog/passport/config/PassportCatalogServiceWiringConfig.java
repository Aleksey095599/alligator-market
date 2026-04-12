package com.alligator.market.backend.provider.catalog.passport.config;

import com.alligator.market.backend.provider.catalog.passport.service.PassportCatalogService;
import com.alligator.market.backend.provider.catalog.passport.service.PassportCatalogServiceImpl;
import com.alligator.market.backend.provider.readmodel.passport.config.query.ProviderPassportQueryPortWiringConfig;
import com.alligator.market.domain.provider.readmodel.passport.query.port.ProviderPassportQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link PassportCatalogService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderPassportQueryPortWiringConfig.class
})
public class PassportCatalogServiceWiringConfig {

    public static final String BEAN_PASSPORT_CATALOG_SERVICE = "passportCatalogService";

    /**
     * Сервис каталога паспортов провайдеров.
     */
    @Bean(BEAN_PASSPORT_CATALOG_SERVICE)
    public PassportCatalogService passportCatalogService(
            @Qualifier(ProviderPassportQueryPortWiringConfig.BEAN_PROVIDER_PASSPORT_QUERY_PORT)
            ProviderPassportQueryPort queryPort
    ) {
        return new PassportCatalogServiceImpl(queryPort);
    }
}
