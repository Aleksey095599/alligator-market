package com.alligator.market.backend.instrument.config.registry.runtime;

import com.alligator.market.backend.instrument.registry.runtime.AtomicRuntimeInstrumentRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RuntimeInstrumentRegistryWiringConfig {
    public static final String BEAN_RUNTIME_INSTRUMENT_REGISTRY =
            "runtimeInstrumentRegistry";
    public static final String BEAN_RUNTIME_INSTRUMENT_REGISTRY_PUBLISHER =
            BEAN_RUNTIME_INSTRUMENT_REGISTRY;

    @Bean(BEAN_RUNTIME_INSTRUMENT_REGISTRY)
    public AtomicRuntimeInstrumentRegistry runtimeInstrumentRegistry() {
        return new AtomicRuntimeInstrumentRegistry();
    }
}
