package com.alligator.market.backend.process.quotemonitor.config;

import com.alligator.market.backend.process.quotemonitor.config.capturer.LiveQuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.QuoteMonitorInstrumentSelectionWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.application.sync.startup.QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.runtime.LiveQuoteMonitorRuntimeControlServiceWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        LiveQuoteMonitorCapturerWiringConfig.class,
        QuoteMonitorInstrumentSelectionWiringConfig.class,
        QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig.class,
        LiveQuoteMonitorRuntimeControlServiceWiringConfig.class
})
public class QuoteMonitorFeatureWiringConfig {
}
