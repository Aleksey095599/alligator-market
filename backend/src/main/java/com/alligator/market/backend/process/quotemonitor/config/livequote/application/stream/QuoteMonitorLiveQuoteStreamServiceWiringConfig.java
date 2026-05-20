package com.alligator.market.backend.process.quotemonitor.config.livequote.application.stream;

import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteStreamService;
import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteUpdateStream;
import com.alligator.market.backend.process.quotemonitor.config.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig.class)
public class QuoteMonitorLiveQuoteStreamServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_LIVE_QUOTE_STREAM_SERVICE =
            "quoteMonitorLiveQuoteStreamService";

    @Bean(BEAN_QUOTE_MONITOR_LIVE_QUOTE_STREAM_SERVICE)
    public QuoteMonitorLiveQuoteStreamService quoteMonitorLiveQuoteStreamService(
            @Qualifier(RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_REGISTRY)
            RuntimeQuoteMonitorLiveQuoteRegistry registry,
            @Qualifier(RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_UPDATE_STREAM)
            QuoteMonitorLiveQuoteUpdateStream updateStream
    ) {
        return new QuoteMonitorLiveQuoteStreamService(registry, updateStream);
    }
}
