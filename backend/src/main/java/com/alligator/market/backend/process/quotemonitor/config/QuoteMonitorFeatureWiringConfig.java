package com.alligator.market.backend.process.quotemonitor.config;

import com.alligator.market.backend.process.quotemonitor.config.capturer.LiveQuoteMonitorCapturerWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.instrument.QuoteMonitorInstrumentWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        LiveQuoteMonitorCapturerWiringConfig.class,
        QuoteMonitorInstrumentWiringConfig.class
})
public class QuoteMonitorFeatureWiringConfig {
}
