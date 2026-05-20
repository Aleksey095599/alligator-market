package com.alligator.market.backend.instrument.config.registry;

import com.alligator.market.backend.instrument.config.registry.application.sync.startup.InstrumentRegistrySynchronizationStartupRunnerWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        StoredInstrumentRegistryWiringConfig.class,
        InstrumentRegistrySynchronizationStartupRunnerWiringConfig.class
})
public class InstrumentRegistryFeatureWiringConfig {
}
