package com.alligator.market.backend.process.quotemonitor.config;

import com.alligator.market.backend.process.quotemonitor.config.capturer.LiveQuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.QuoteMonitorInstrumentSelectionWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        LiveQuoteMonitorCapturerWiringConfig.class,
        QuoteMonitorInstrumentSelectionWiringConfig.class
})
public class QuoteMonitorFeatureWiringConfig {
}
