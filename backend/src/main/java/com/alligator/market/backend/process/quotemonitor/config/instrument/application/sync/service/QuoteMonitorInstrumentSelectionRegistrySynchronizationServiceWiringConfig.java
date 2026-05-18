package com.alligator.market.backend.process.quotemonitor.config.instrument.application.sync.service;

import com.alligator.market.backend.process.quotemonitor.application.instrument.sync.QuoteMonitorInstrumentSelectionRegistrySynchronizationService;
import com.alligator.market.backend.process.quotemonitor.config.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig.class)
public class QuoteMonitorInstrumentSelectionRegistrySynchronizationServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_SYNCHRONIZATION_SERVICE =
            "quoteMonitorInstrumentSelectionRegistrySynchronizationService";

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_SYNCHRONIZATION_SERVICE)
    public QuoteMonitorInstrumentSelectionRegistrySynchronizationService
            quoteMonitorInstrumentSelectionRegistrySynchronizationService(
            @Qualifier(
                    RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig
                            .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_UPDATER
            )
            RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater
    ) {
        return new QuoteMonitorInstrumentSelectionRegistrySynchronizationService(runtimeRegistryUpdater);
    }
}
