package com.alligator.market.backend.source.config;

import com.alligator.market.backend.source.config.adapter.moex.iss.MoexIssSourceConfig;
import com.alligator.market.backend.source.config.passport.application.query.list.PassportListServiceWiringConfig;
import com.alligator.market.backend.source.config.passport.application.sync.startup.SourcePassportRegistrySynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.source.config.registry.RuntimeSourceRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeSourceRegistryWiringConfig.class,
        PassportListServiceWiringConfig.class,
        SourcePassportRegistrySynchronizationStartupRunnerWiringConfig.class,
        MoexIssSourceConfig.class
})
public class SourceFeatureWiringConfig {
}
