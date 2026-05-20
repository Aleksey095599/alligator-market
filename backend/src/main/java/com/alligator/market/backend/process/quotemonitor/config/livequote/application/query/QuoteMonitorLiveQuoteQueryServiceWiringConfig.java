package com.alligator.market.backend.process.quotemonitor.config.livequote.application.query;

import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteQueryService;
import com.alligator.market.backend.process.quotemonitor.config.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig.class)
public class QuoteMonitorLiveQuoteQueryServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_LIVE_QUOTE_QUERY_SERVICE =
            "quoteMonitorLiveQuoteQueryService";

    @Bean(BEAN_QUOTE_MONITOR_LIVE_QUOTE_QUERY_SERVICE)
    public QuoteMonitorLiveQuoteQueryService quoteMonitorLiveQuoteQueryService(
            @Qualifier(RuntimeQuoteMonitorLiveQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_LIVE_QUOTE_REGISTRY)
            RuntimeQuoteMonitorLiveQuoteRegistry registry
    ) {
        return new QuoteMonitorLiveQuoteQueryService(registry);
    }
}
