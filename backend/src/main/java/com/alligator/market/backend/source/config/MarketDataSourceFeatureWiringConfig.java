package com.alligator.market.backend.source.config;

import com.alligator.market.backend.source.config.adapter.moex.iss.MoexIssMarketDataSourceConfig;
import com.alligator.market.backend.source.config.passport.application.query.list.PassportListServiceWiringConfig;
import com.alligator.market.backend.source.config.passport.application.projection.startup.MarketDataSourcePassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.source.config.registry.MarketDataSourceRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи provider.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourceRegistryWiringConfig.class,
        PassportListServiceWiringConfig.class,
        MarketDataSourcePassportProjectionStartupRunnerWiringConfig.class,
        MoexIssMarketDataSourceConfig.class
})
public class MarketDataSourceFeatureWiringConfig {
}
