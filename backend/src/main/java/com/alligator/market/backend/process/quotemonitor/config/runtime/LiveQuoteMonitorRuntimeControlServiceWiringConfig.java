package com.alligator.market.backend.process.quotemonitor.config.runtime;

import com.alligator.market.backend.process.quotemonitor.application.runtime.LiveQuoteMonitorRuntimeControlService;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeProcess;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(LiveQuoteMonitorRuntimeProcessWiringConfig.class)
public class LiveQuoteMonitorRuntimeControlServiceWiringConfig {
    public static final String BEAN_LIVE_QUOTE_MONITOR_RUNTIME_CONTROL_SERVICE =
            "liveQuoteMonitorRuntimeControlService";

    @Bean(BEAN_LIVE_QUOTE_MONITOR_RUNTIME_CONTROL_SERVICE)
    public LiveQuoteMonitorRuntimeControlService liveQuoteMonitorRuntimeControlService(
            @Qualifier(LiveQuoteMonitorRuntimeProcessWiringConfig.BEAN_LIVE_QUOTE_MONITOR_RUNTIME_PROCESS)
            LiveQuoteMonitorRuntimeProcess runtimeProcess
    ) {
        return new LiveQuoteMonitorRuntimeControlService(runtimeProcess);
    }
}
