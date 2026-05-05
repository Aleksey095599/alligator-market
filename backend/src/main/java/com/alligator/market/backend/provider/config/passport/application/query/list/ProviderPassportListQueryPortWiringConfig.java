package com.alligator.market.backend.provider.config.passport.application.query.list;

import com.alligator.market.backend.provider.passport.application.query.list.adapter.JooqProviderPassportListQueryAdapter;
import com.alligator.market.backend.provider.passport.application.query.list.port.ProviderPassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring configuration for provider passport list query port.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportListQueryPortWiringConfig {

    public static final String BEAN_PROVIDER_PASSPORT_LIST_QUERY_PORT =
            "providerPassportListQueryPort";

    @Bean(BEAN_PROVIDER_PASSPORT_LIST_QUERY_PORT)
    public ProviderPassportListQueryPort providerPassportListQueryPort(DSLContext dsl) {
        return new JooqProviderPassportListQueryAdapter(dsl);
    }
}
