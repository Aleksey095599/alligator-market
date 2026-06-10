package com.alligator.market.backend.source.config;

import com.alligator.market.backend.source.config.adapter.moex.iss.MoexIssSourceConfig;
import com.alligator.market.backend.source.config.adapter.twelvedata.TwelveDataSourceConfig;
import com.alligator.market.backend.source.config.handler.passport.application.query.list.SourceHandlerPassportListServiceWiringConfig;
import com.alligator.market.backend.source.config.passport.application.query.list.PassportListServiceWiringConfig;
import com.alligator.market.backend.source.config.passport.application.sync.startup.SourcePassportStoreSynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.source.config.registry.RuntimeSourceRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeSourceRegistryWiringConfig.class,
        PassportListServiceWiringConfig.class,
        SourceHandlerPassportListServiceWiringConfig.class,
        SourcePassportStoreSynchronizationStartupRunnerWiringConfig.class,
        MoexIssSourceConfig.class,
        TwelveDataSourceConfig.class
})
public class SourceFeatureWiringConfig {
}
