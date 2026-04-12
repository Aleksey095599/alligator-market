package com.alligator.market.backend.provider.readmodel.passport.config.projection.jdbc;

import com.alligator.market.backend.provider.readmodel.passport.projection.port.adapter.jdbc.ProviderPassportProjectionWritePortJdbcAdapter;
import com.alligator.market.domain.provider.readmodel.passport.projection.port.ProviderPassportProjectionWritePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
    public ProviderPassportProjectionWritePort providerPassportProjectionWritePort(JdbcTemplate jdbcTemplate) {
        return new ProviderPassportProjectionWritePortJdbcAdapter(jdbcTemplate);
    }
}
