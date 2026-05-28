package com.alligator.market.backend.process.quotemonitor.config.runtime;

import com.alligator.market.backend.instrument.config.registry.runtime.RuntimeInstrumentRegistryWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.capturer.QuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig;
import com.alligator.market.backend.source.config.registry.RuntimeMarketDataSourceRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.runtime.RuntimeSourcePlanRegistryWiringConfig;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.start.RegistryBackedQuoteMonitorRuntimeStart;
import com.alligator.market.domain.process.quotemonitor.runtime.start.QuoteMonitorRuntimeStart;
import com.alligator.market.domain.source.registry.RuntimeMarketDataSourceRegistry;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        QuoteMonitorCapturerWiringConfig.class,
        RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig.class,
        RuntimeInstrumentRegistryWiringConfig.class,
        RuntimeSourcePlanRegistryWiringConfig.class,
        RuntimeMarketDataSourceRegistryWiringConfig.class
})
public class QuoteMonitorRuntimeStartWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_RUNTIME_START =
            "quoteMonitorRuntimeStart";

    @Bean(BEAN_QUOTE_MONITOR_RUNTIME_START)
    public QuoteMonitorRuntimeStart quoteMonitorRuntimeStart(
            @Qualifier(QuoteMonitorCapturerWiringConfig.BEAN_QUOTE_MONITOR_CAPTURER)
            QuoteMonitorCapturer capturer,
            @Qualifier(RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY)
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            @Qualifier(RuntimeInstrumentRegistryWiringConfig.BEAN_RUNTIME_INSTRUMENT_REGISTRY)
            RuntimeInstrumentRegistry instrumentRegistry,
            @Qualifier(RuntimeSourcePlanRegistryWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY)
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            @Qualifier(RuntimeMarketDataSourceRegistryWiringConfig.BEAN_RUNTIME_MARKET_DATA_SOURCE_REGISTRY)
            RuntimeMarketDataSourceRegistry sourceRegistry
    ) {
        return new RegistryBackedQuoteMonitorRuntimeStart(
                capturer,
                instrumentSelectionRegistry,
                instrumentRegistry,
                sourcePlanRegistry,
                sourceRegistry
        );
    }
}
