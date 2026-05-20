package com.alligator.market.backend.process.quotemonitor.config.livequote.registry.runtime;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorLiveQuoteRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig {
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_REGISTRY =
            "runtimeQuoteMonitorLiveQuoteRegistry";
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_PUBLISHER =
            BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_REGISTRY;

    @Bean(BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_REGISTRY)
    public AtomicRuntimeQuoteMonitorLiveQuoteRegistry runtimeQuoteMonitorLiveQuoteRegistry() {
        return new AtomicRuntimeQuoteMonitorLiveQuoteRegistry();
    }
}
