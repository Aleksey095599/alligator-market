package com.alligator.market.backend.marketdata.provider.readmodel.passport.config.query;

import com.alligator.market.backend.marketdata.provider.readmodel.passport.query.port.adapter.jdbc.ProviderPassportQueryPortJdbcAdapter;
import com.alligator.market.domain.marketdata.provider.readmodel.passport.query.port.ProviderPassportQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Конфигурация wiring {@link ProviderPassportQueryPort}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportQueryPortWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_QUERY_PORT = "providerPassportQueryPort";

    /**
     * Query-порт чтения паспортов провайдеров из materialized view.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_QUERY_PORT)
    public ProviderPassportQueryPort providerPassportQueryPort(JdbcTemplate jdbcTemplate) {
        return new ProviderPassportQueryPortJdbcAdapter(jdbcTemplate);
    }
}
