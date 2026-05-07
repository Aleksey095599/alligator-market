package com.alligator.market.backend.source.config.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.adapter.JooqMarketDataSourcePassportListQueryAdapter;
import com.alligator.market.backend.source.passport.application.query.list.port.MarketDataSourcePassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring configuration for source passport list query port.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourcePassportListQueryPortWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_PASSPORT_LIST_QUERY_PORT =
            "marketDataSourcePassportListQueryPort";

    @Bean(BEAN_MARKET_DATA_SOURCE_PASSPORT_LIST_QUERY_PORT)
    public MarketDataSourcePassportListQueryPort marketDataSourcePassportListQueryPort(DSLContext dsl) {
        return new JooqMarketDataSourcePassportListQueryAdapter(dsl);
    }
}
