package com.alligator.market.backend.provider.config.passport.application.query.list;

import com.alligator.market.backend.provider.passport.application.query.list.PassportListService;
import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link PassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class
})
public class PassportListServiceWiringConfig {

    public static final String BEAN_PASSPORT_LIST_SERVICE = "passportListService";

    /**
     * Сервис списка паспортов провайдеров.
     */
    @Bean(BEAN_PASSPORT_LIST_SERVICE)
    public PassportListService passportListService(
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry
    ) {
        return new PassportListService(providerRegistry);
    }
}
