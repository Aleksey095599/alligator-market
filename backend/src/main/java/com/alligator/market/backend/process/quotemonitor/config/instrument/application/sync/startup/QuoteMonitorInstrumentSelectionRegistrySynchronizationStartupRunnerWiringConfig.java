package com.alligator.market.backend.process.quotemonitor.config.instrument.application.sync.startup;

import com.alligator.market.backend.process.quotemonitor.application.instrument.sync.QuoteMonitorInstrumentSelectionRegistrySynchronizationService;
import com.alligator.market.backend.process.quotemonitor.application.instrument.sync.runner.QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunner;
import com.alligator.market.backend.process.quotemonitor.config.instrument.application.sync.service.QuoteMonitorInstrumentSelectionRegistrySynchronizationServiceWiringConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(QuoteMonitorInstrumentSelectionRegistrySynchronizationServiceWiringConfig.class)
public class QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER =
            "quoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunner";

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER)
    public ApplicationRunner quoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunner(
            @Qualifier(QuoteMonitorInstrumentSelectionRegistrySynchronizationServiceWiringConfig
                    .BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_SYNCHRONIZATION_SERVICE)
            QuoteMonitorInstrumentSelectionRegistrySynchronizationService service
    ) {
        return new QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunner(service);
    }
}
