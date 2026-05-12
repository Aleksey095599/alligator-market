package com.alligator.market.backend.source.config;

import com.alligator.market.backend.source.config.adapter.moex.iss.MoexIssSourceConfig;
import com.alligator.market.backend.source.config.passport.application.query.list.PassportListServiceWiringConfig;
import com.alligator.market.backend.source.config.passport.application.projection.startup.SourcePassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.source.config.registry.SourceRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourceRegistryWiringConfig.class,
        PassportListServiceWiringConfig.class,
        SourcePassportProjectionStartupRunnerWiringConfig.class,
        MoexIssSourceConfig.class
})
public class SourceFeatureWiringConfig {
}
