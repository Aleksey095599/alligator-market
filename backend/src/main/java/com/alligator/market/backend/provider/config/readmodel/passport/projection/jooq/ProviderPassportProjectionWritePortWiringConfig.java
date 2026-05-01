package com.alligator.market.backend.provider.config.readmodel.passport.projection.jooq;

import com.alligator.market.backend.provider.infrastructure.persistence.passport.readmodel.jooq.ProviderPassportProjectionWritePortJooqAdapter;
import com.alligator.market.backend.provider.application.passport.projection.port.out.ProviderPassportProjectionWritePort;
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
        return new ProviderPassportProjectionWritePortJooqAdapter(dsl);
    }
}
