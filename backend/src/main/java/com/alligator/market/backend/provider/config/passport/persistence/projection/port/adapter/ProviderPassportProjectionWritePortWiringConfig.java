package com.alligator.market.backend.provider.config.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.provider.passport.persistence.projection.port.adapter.JooqProviderPassportProjectionWritePortAdapter;
import com.alligator.market.backend.provider.passport.application.projection.port.ProviderPassportProjectionWritePort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link ProviderPassportProjectionWritePort}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportProjectionWritePortWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_WRITE_PORT =
            "providerPassportProjectionWritePort";

    /**
     * Write-порт проекции паспортов провайдеров.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_WRITE_PORT)
    public ProviderPassportProjectionWritePort providerPassportProjectionWritePort(DSLContext dsl) {
        return new JooqProviderPassportProjectionWritePortAdapter(dsl);
    }
}
