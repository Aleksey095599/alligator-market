package com.alligator.market.backend.process.quotemonitor.config.instrument.registry.runtime;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig {
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY =
            "runtimeQuoteMonitorInstrumentSelectionRegistry";
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_PUBLISHER =
            BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY;

    @Bean(BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY)
    public AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry runtimeQuoteMonitorInstrumentSelectionRegistry() {
        return new AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry();
    }
}
