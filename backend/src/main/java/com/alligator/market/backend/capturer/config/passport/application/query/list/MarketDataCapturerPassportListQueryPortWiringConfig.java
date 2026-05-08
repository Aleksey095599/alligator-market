package com.alligator.market.backend.capturer.config.passport.application.query.list;

import com.alligator.market.backend.capturer.passport.application.query.list.adapter.JooqMarketDataCapturerPassportListQueryAdapter;
import com.alligator.market.backend.capturer.passport.application.query.list.port.MarketDataCapturerPassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring configuration for capturer passport list query port.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataCapturerPassportListQueryPortWiringConfig {

    public static final String BEAN_CAPTURER_PASSPORT_LIST_QUERY_PORT =
            "capturerPassportListQueryPort";

    @Bean(BEAN_CAPTURER_PASSPORT_LIST_QUERY_PORT)
    public MarketDataCapturerPassportListQueryPort capturerPassportListQueryPort(DSLContext dsl) {
        return new JooqMarketDataCapturerPassportListQueryAdapter(dsl);
    }
}
