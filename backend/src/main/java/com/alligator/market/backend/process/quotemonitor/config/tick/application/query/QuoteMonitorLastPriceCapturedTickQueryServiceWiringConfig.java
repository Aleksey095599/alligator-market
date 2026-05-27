package com.alligator.market.backend.process.quotemonitor.config.tick.application.query;

import com.alligator.market.backend.process.quotemonitor.application.tick.QuoteMonitorLastPriceCapturedTickQueryService;
import com.alligator.market.backend.process.quotemonitor.config.tick.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig.class)
public class QuoteMonitorLastPriceCapturedTickQueryServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_QUERY_SERVICE =
            "quoteMonitorLastPriceCapturedTickQueryService";

    @Bean(BEAN_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_QUERY_SERVICE)
    public QuoteMonitorLastPriceCapturedTickQueryService quoteMonitorLastPriceCapturedTickQueryService(
            @Qualifier(RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_REGISTRY)
            RuntimeQuoteMonitorLastPriceCapturedTickRegistry registry
    ) {
        return new QuoteMonitorLastPriceCapturedTickQueryService(registry);
    }
}
