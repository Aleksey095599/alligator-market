package com.alligator.market.backend.process.quotemonitor.config.instrument.registry.sync;

import com.alligator.market.backend.process.quotemonitor.config.instrument.persistence.jooq.registry.StoredQuoteMonitorInstrumentSelectionRegistryWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.stored.StoredQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        StoredQuoteMonitorInstrumentSelectionRegistryWiringConfig.class,
        RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig.class
})
public class RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig {
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_UPDATER =
            "runtimeQuoteMonitorInstrumentSelectionRegistryUpdater";

    @Bean(BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_UPDATER)
    public RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeQuoteMonitorInstrumentSelectionRegistryUpdater(
            @Qualifier(
                    StoredQuoteMonitorInstrumentSelectionRegistryWiringConfig
                            .BEAN_STORED_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY
            )
            StoredQuoteMonitorInstrumentSelectionRegistry storedRegistry,
            @Qualifier(
                    RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig
                            .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_PUBLISHER
            )
            RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher runtimeRegistryPublisher
    ) {
        return new SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(
                storedRegistry,
                runtimeRegistryPublisher
        );
    }
}
