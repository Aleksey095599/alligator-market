package com.alligator.market.backend.process.quotemonitor.config.runtime;

import com.alligator.market.backend.process.quotemonitor.runtime.LiveQuoteMonitorProcessScheduler;
import com.alligator.market.backend.process.quotemonitor.runtime.ScheduledExecutorLiveQuoteMonitorProcessScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LiveQuoteMonitorRuntimeSchedulerWiringConfig {
    public static final String BEAN_LIVE_QUOTE_MONITOR_PROCESS_SCHEDULER =
            "liveQuoteMonitorProcessScheduler";

    @Bean(BEAN_LIVE_QUOTE_MONITOR_PROCESS_SCHEDULER)
    public ScheduledExecutorLiveQuoteMonitorProcessScheduler liveQuoteMonitorProcessScheduler() {
        return new ScheduledExecutorLiveQuoteMonitorProcessScheduler();
    }
}
