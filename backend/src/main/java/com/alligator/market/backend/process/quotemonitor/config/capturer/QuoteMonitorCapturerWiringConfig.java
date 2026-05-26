package com.alligator.market.backend.process.quotemonitor.config.capturer;

import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class QuoteMonitorCapturerWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_CAPTURER =
            "quoteMonitorCapturer";

    @Bean(BEAN_QUOTE_MONITOR_CAPTURER)
    public QuoteMonitorCapturer quoteMonitorCapturer() {
        return new QuoteMonitorCapturer();
    }
}
