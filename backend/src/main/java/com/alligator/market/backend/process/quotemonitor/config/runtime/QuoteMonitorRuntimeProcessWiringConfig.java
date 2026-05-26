package com.alligator.market.backend.process.quotemonitor.config.runtime;

import com.alligator.market.backend.instrument.config.registry.runtime.RuntimeInstrumentRegistryWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.capturer.QuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig;
import com.alligator.market.backend.process.quotemonitor.runtime.DefaultQuoteMonitorRuntimeProcess;
import com.alligator.market.backend.source.config.registry.RuntimeSourceRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.runtime.RuntimeSourcePlanRegistryWiringConfig;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuotePublisher;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
@Import({
        QuoteMonitorCapturerWiringConfig.class,
        RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig.class,
        RuntimeInstrumentRegistryWiringConfig.class,
        RuntimeSourceRegistryWiringConfig.class,
        RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig.class,
        RuntimeSourcePlanRegistryWiringConfig.class
})
public class QuoteMonitorRuntimeProcessWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_RUNTIME_PROCESS =
            "quoteMonitorRuntimeProcess";
    public static final String BEAN_QUOTE_MONITOR_RUNTIME_CLOCK =
            "quoteMonitorRuntimeClock";

    @Bean(BEAN_QUOTE_MONITOR_RUNTIME_CLOCK)
    public Clock quoteMonitorRuntimeClock() {
        return Clock.systemUTC();
    }

    @Bean(BEAN_QUOTE_MONITOR_RUNTIME_PROCESS)
    public QuoteMonitorRuntimeProcess quoteMonitorRuntimeProcess(
            @Qualifier(QuoteMonitorCapturerWiringConfig.BEAN_QUOTE_MONITOR_CAPTURER)
            QuoteMonitorCapturer capturer,
            @Qualifier(RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY)
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            @Qualifier(RuntimeInstrumentRegistryWiringConfig.BEAN_RUNTIME_INSTRUMENT_REGISTRY)
            RuntimeInstrumentRegistry instrumentRegistry,
            @Qualifier(RuntimeSourcePlanRegistryWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY)
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            @Qualifier(RuntimeSourceRegistryWiringConfig.BEAN_RUNTIME_SOURCE_REGISTRY)
            RuntimeSourceRegistry sourceRegistry,
            @Qualifier(RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_PUBLISHER)
            RuntimeQuoteMonitorInstrumentQuotePublisher instrumentQuotePublisher,
            @Qualifier(BEAN_QUOTE_MONITOR_RUNTIME_CLOCK)
            Clock clock
    ) {
        return new DefaultQuoteMonitorRuntimeProcess(
                capturer,
                instrumentSelectionRegistry,
                instrumentRegistry,
                sourcePlanRegistry,
                sourceRegistry,
                instrumentQuotePublisher,
                clock
        );
    }
}
