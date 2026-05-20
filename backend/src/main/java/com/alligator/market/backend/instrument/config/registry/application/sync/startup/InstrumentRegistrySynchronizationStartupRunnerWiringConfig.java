package com.alligator.market.backend.instrument.config.registry.application.sync.startup;

import com.alligator.market.backend.instrument.application.registry.sync.InstrumentRegistrySynchronizationService;
import com.alligator.market.backend.instrument.application.registry.sync.runner.InstrumentRegistrySynchronizationStartupRunner;
import com.alligator.market.backend.instrument.config.registry.application.sync.service.InstrumentRegistrySynchronizationServiceWiringConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(InstrumentRegistrySynchronizationServiceWiringConfig.class)
public class InstrumentRegistrySynchronizationStartupRunnerWiringConfig {
    public static final String BEAN_INSTRUMENT_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER =
            "instrumentRegistrySynchronizationStartupRunner";

    @Bean(BEAN_INSTRUMENT_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER)
    public ApplicationRunner instrumentRegistrySynchronizationStartupRunner(
            @Qualifier(InstrumentRegistrySynchronizationServiceWiringConfig
                    .BEAN_INSTRUMENT_REGISTRY_SYNCHRONIZATION_SERVICE)
            InstrumentRegistrySynchronizationService service
    ) {
        return new InstrumentRegistrySynchronizationStartupRunner(service);
    }
}
