package com.alligator.market.backend.process.quotemonitor.config.quote.registry.runtime;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorInstrumentQuoteRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig {
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_REGISTRY =
            "runtimeQuoteMonitorInstrumentQuoteRegistry";
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_PUBLISHER =
            BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_REGISTRY;
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_UPDATE_STREAM =
            BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_REGISTRY;

    @Bean(BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_REGISTRY)
    public AtomicRuntimeQuoteMonitorInstrumentQuoteRegistry runtimeQuoteMonitorInstrumentQuoteRegistry() {
        return new AtomicRuntimeQuoteMonitorInstrumentQuoteRegistry();
    }
}
