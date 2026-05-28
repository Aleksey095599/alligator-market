package com.alligator.market.backend.source.config;

import com.alligator.market.backend.source.config.adapter.moex.iss.MoexIssMarketDataSourceConfig;
import com.alligator.market.backend.source.config.adapter.twelvedata.TwelveDataMarketDataSourceConfig;
import com.alligator.market.backend.source.config.passport.application.query.list.PassportListServiceWiringConfig;
import com.alligator.market.backend.source.config.passport.application.sync.startup.SourcePassportRegistrySynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.source.config.registry.RuntimeMarketDataSourceRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeMarketDataSourceRegistryWiringConfig.class,
        PassportListServiceWiringConfig.class,
        SourcePassportRegistrySynchronizationStartupRunnerWiringConfig.class,
        MoexIssMarketDataSourceConfig.class,
        TwelveDataMarketDataSourceConfig.class
})
public class SourceFeatureWiringConfig {
}
