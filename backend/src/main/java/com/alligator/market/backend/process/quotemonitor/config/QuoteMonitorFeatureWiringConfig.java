package com.alligator.market.backend.process.quotemonitor.config;

import com.alligator.market.backend.process.quotemonitor.config.capturer.QuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.QuoteMonitorInstrumentSelectionWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.application.sync.startup.QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.livequote.application.query.QuoteMonitorLiveQuoteQueryServiceWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.livequote.application.stream.QuoteMonitorLiveQuoteStreamServiceWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.runtime.QuoteMonitorRuntimeControlServiceWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        QuoteMonitorCapturerWiringConfig.class,
        QuoteMonitorInstrumentSelectionWiringConfig.class,
        QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig.class,
        QuoteMonitorLiveQuoteQueryServiceWiringConfig.class,
        QuoteMonitorLiveQuoteStreamServiceWiringConfig.class,
        QuoteMonitorRuntimeControlServiceWiringConfig.class
})
public class QuoteMonitorFeatureWiringConfig {
}
