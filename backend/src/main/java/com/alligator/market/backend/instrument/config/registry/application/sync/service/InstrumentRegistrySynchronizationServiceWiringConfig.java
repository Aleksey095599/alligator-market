package com.alligator.market.backend.instrument.config.registry.application.sync.service;

import com.alligator.market.backend.instrument.application.registry.sync.InstrumentRegistrySynchronizationService;
import com.alligator.market.backend.instrument.config.registry.sync.RuntimeInstrumentRegistryUpdaterWiringConfig;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeInstrumentRegistryUpdaterWiringConfig.class)
public class InstrumentRegistrySynchronizationServiceWiringConfig {
    public static final String BEAN_INSTRUMENT_REGISTRY_SYNCHRONIZATION_SERVICE =
            "instrumentRegistrySynchronizationService";

    @Bean(BEAN_INSTRUMENT_REGISTRY_SYNCHRONIZATION_SERVICE)
    public InstrumentRegistrySynchronizationService instrumentRegistrySynchronizationService(
            @Qualifier(RuntimeInstrumentRegistryUpdaterWiringConfig.BEAN_RUNTIME_INSTRUMENT_REGISTRY_UPDATER)
            RuntimeInstrumentRegistryUpdater runtimeRegistryUpdater
    ) {
        return new InstrumentRegistrySynchronizationService(runtimeRegistryUpdater);
    }
}
