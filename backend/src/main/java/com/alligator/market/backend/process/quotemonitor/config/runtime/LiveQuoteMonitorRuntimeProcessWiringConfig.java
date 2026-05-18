package com.alligator.market.backend.process.quotemonitor.config.runtime;

import com.alligator.market.backend.process.quotemonitor.config.capturer.LiveQuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig;
import com.alligator.market.backend.process.quotemonitor.runtime.LiveQuoteMonitorProcessScheduler;
import com.alligator.market.backend.process.quotemonitor.runtime.ScheduledLiveQuoteMonitorRuntimeProcess;
import com.alligator.market.backend.sourceplan.config.plan.registry.runtime.RuntimeSourcePlanRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.capturer.LiveQuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeProcess;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
@Import({
        LiveQuoteMonitorCapturerWiringConfig.class,
        RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig.class,
        RuntimeSourcePlanRegistryWiringConfig.class,
        LiveQuoteMonitorRuntimeSchedulerWiringConfig.class
})
public class LiveQuoteMonitorRuntimeProcessWiringConfig {
    public static final String BEAN_LIVE_QUOTE_MONITOR_RUNTIME_PROCESS =
            "liveQuoteMonitorRuntimeProcess";
    public static final String BEAN_LIVE_QUOTE_MONITOR_RUNTIME_CLOCK =
            "liveQuoteMonitorRuntimeClock";

    @Bean(BEAN_LIVE_QUOTE_MONITOR_RUNTIME_CLOCK)
    public Clock liveQuoteMonitorRuntimeClock() {
        return Clock.systemUTC();
    }

    @Bean(BEAN_LIVE_QUOTE_MONITOR_RUNTIME_PROCESS)
    public LiveQuoteMonitorRuntimeProcess liveQuoteMonitorRuntimeProcess(
            @Qualifier(LiveQuoteMonitorCapturerWiringConfig.BEAN_LIVE_QUOTE_MONITOR_CAPTURER)
            LiveQuoteMonitorCapturer capturer,
            @Qualifier(RuntimeQuoteMonitorInstrumentSelectionRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY)
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            @Qualifier(RuntimeSourcePlanRegistryWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY)
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            @Qualifier(LiveQuoteMonitorRuntimeSchedulerWiringConfig.BEAN_LIVE_QUOTE_MONITOR_PROCESS_SCHEDULER)
            LiveQuoteMonitorProcessScheduler scheduler,
            @Qualifier(BEAN_LIVE_QUOTE_MONITOR_RUNTIME_CLOCK)
            Clock clock
    ) {
        return new ScheduledLiveQuoteMonitorRuntimeProcess(
                capturer,
                instrumentSelectionRegistry,
                sourcePlanRegistry,
                scheduler,
                clock
        );
    }
}
