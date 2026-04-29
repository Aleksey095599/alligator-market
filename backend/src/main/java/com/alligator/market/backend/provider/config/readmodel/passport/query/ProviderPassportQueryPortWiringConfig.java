package com.alligator.market.backend.provider.config.readmodel.passport.query;

import com.alligator.market.backend.provider.readmodel.passport.query.port.adapter.jooq.ProviderPassportQueryPortJooqAdapter;
import com.alligator.market.backend.provider.application.passport.catalog.port.out.ProviderPassportQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link ProviderPassportQueryPort}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportQueryPortWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_QUERY_PORT = "providerPassportQueryPort";

    /**
     * Query-порт чтения паспортов провайдеров из materialized view.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_QUERY_PORT)
    public ProviderPassportQueryPort providerPassportQueryPort(DSLContext dsl) {
        return new ProviderPassportQueryPortJooqAdapter(dsl);
    }
}
