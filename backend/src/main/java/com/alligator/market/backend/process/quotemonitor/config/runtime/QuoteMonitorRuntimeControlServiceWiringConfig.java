package com.alligator.market.backend.process.quotemonitor.config.runtime;

import com.alligator.market.backend.process.quotemonitor.application.runtime.QuoteMonitorRuntimeControlService;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(QuoteMonitorRuntimeProcessWiringConfig.class)
public class QuoteMonitorRuntimeControlServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_RUNTIME_CONTROL_SERVICE =
            "quoteMonitorRuntimeControlService";

    @Bean(BEAN_QUOTE_MONITOR_RUNTIME_CONTROL_SERVICE)
    public QuoteMonitorRuntimeControlService quoteMonitorRuntimeControlService(
            @Qualifier(QuoteMonitorRuntimeProcessWiringConfig.BEAN_QUOTE_MONITOR_RUNTIME_PROCESS)
            QuoteMonitorRuntimeProcess runtimeProcess
    ) {
        return new QuoteMonitorRuntimeControlService(runtimeProcess);
    }
}
