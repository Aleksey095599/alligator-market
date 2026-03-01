package com.alligator.market.backend.provider.readmodel.passport.config.store;

import com.alligator.market.backend.provider.readmodel.passport.store.ProviderPassportProjectionWriteStoreJdbcAdapter;
import com.alligator.market.domain.provider.readmodel.passport.projection.port.ProviderPassportProjectionWriteStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Конфигурация wiring {@link ProviderPassportProjectionWriteStore}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportProjectionWriteStoreWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_PROJECTION_WRITE_STORE =
            "providerPassportProjectionWriteStore";

    /**
     * Write-порт проекции паспортов провайдеров.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_PROJECTION_WRITE_STORE)
    public ProviderPassportProjectionWriteStore providerPassportProjectionWriteStore(JdbcTemplate jdbcTemplate) {
        return new ProviderPassportProjectionWriteStoreJdbcAdapter(jdbcTemplate);
    }
}
