package com.alligator.market.backend.process.quotemonitor.config.capturer;

import com.alligator.market.domain.process.quotemonitor.LiveQuoteMonitorCapturer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LiveQuoteMonitorCapturerWiringConfig {
    public static final String BEAN_LIVE_QUOTE_MONITOR_CAPTURER =
            "liveQuoteMonitorCapturer";

    @Bean(BEAN_LIVE_QUOTE_MONITOR_CAPTURER)
    public LiveQuoteMonitorCapturer liveQuoteMonitorCapturer() {
        return new LiveQuoteMonitorCapturer();
    }
}
