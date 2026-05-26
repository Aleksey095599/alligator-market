package com.alligator.market.backend.process.quotemonitor.config;

import com.alligator.market.backend.process.quotemonitor.config.capturer.QuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.QuoteMonitorInstrumentSelectionWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.application.sync.startup.QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.quote.application.query.QuoteMonitorInstrumentQuoteQueryServiceWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.quote.application.stream.QuoteMonitorInstrumentQuoteStreamServiceWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.runtime.QuoteMonitorRuntimeControlServiceWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        QuoteMonitorCapturerWiringConfig.class,
        QuoteMonitorInstrumentSelectionWiringConfig.class,
        QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunnerWiringConfig.class,
        QuoteMonitorInstrumentQuoteQueryServiceWiringConfig.class,
        QuoteMonitorInstrumentQuoteStreamServiceWiringConfig.class,
        QuoteMonitorRuntimeControlServiceWiringConfig.class
})
public class QuoteMonitorFeatureWiringConfig {
}
