package com.alligator.market.backend.process.quotemonitor.config.tick.registry.runtime;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig {
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_REGISTRY =
            "runtimeQuoteMonitorLastPriceCapturedTickRegistry";
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_PUBLISHER =
            BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_REGISTRY;
    public static final String BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_UPDATE_STREAM =
            BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_REGISTRY;

    @Bean(BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_REGISTRY)
    public AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry runtimeQuoteMonitorLastPriceCapturedTickRegistry() {
        return new AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry();
    }
}
