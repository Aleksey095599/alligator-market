package com.alligator.market.backend.provider.config;

import com.alligator.market.backend.provider.config.adapter.moex.iss.MoexIssProviderConfig;
import com.alligator.market.backend.provider.config.application.passport.query.list.PassportListServiceWiringConfig;
import com.alligator.market.backend.provider.config.readmodel.passport.projection.startup.ProviderPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи provider.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderRegistryWiringConfig.class,
        PassportListServiceWiringConfig.class,
        ProviderPassportProjectionStartupRunnerWiringConfig.class,
        MoexIssProviderConfig.class
})
public class ProviderFeatureWiringConfig {
}
