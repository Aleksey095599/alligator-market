package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqMarketDataSourceLifecycleStatusSyncAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring configuration for {@link MarketDataSourceLifecycleStatusSyncPort}.
 */
@Configuration(proxyBeanMethods = false)
public class MarketDataSourceLifecycleStatusSyncPortWiringConfig {

    public static final String BEAN_MARKET_DATA_SOURCE_LIFECYCLE_STATUS_SYNC_PORT =
            "marketDataSourceLifecycleStatusSyncPort";

    @Bean(BEAN_MARKET_DATA_SOURCE_LIFECYCLE_STATUS_SYNC_PORT)
    public MarketDataSourceLifecycleStatusSyncPort marketDataSourceLifecycleStatusSyncPort(DSLContext dsl) {
        return new JooqMarketDataSourceLifecycleStatusSyncAdapter(dsl);
    }
}
