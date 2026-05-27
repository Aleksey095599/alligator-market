package com.alligator.market.backend.process.quotemonitor.config.tick.application.stream;

import com.alligator.market.backend.process.quotemonitor.application.tick.QuoteMonitorLastPriceCapturedTickStreamService;
import com.alligator.market.backend.process.quotemonitor.application.tick.QuoteMonitorLastPriceCapturedTickUpdateStream;
import com.alligator.market.backend.process.quotemonitor.config.tick.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig.class)
public class QuoteMonitorLastPriceCapturedTickStreamServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_STREAM_SERVICE =
            "quoteMonitorLastPriceCapturedTickStreamService";

    @Bean(BEAN_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_STREAM_SERVICE)
    public QuoteMonitorLastPriceCapturedTickStreamService quoteMonitorLastPriceCapturedTickStreamService(
            @Qualifier(RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_REGISTRY)
            RuntimeQuoteMonitorLastPriceCapturedTickRegistry registry,
            @Qualifier(RuntimeQuoteMonitorLastPriceCapturedTickRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_LAST_PRICE_CAPTURED_TICK_UPDATE_STREAM)
            QuoteMonitorLastPriceCapturedTickUpdateStream updateStream
    ) {
        return new QuoteMonitorLastPriceCapturedTickStreamService(registry, updateStream);
    }
}
