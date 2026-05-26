package com.alligator.market.backend.process.quotemonitor.config.quote.application.query;

import com.alligator.market.backend.process.quotemonitor.application.quote.QuoteMonitorInstrumentQuoteQueryService;
import com.alligator.market.backend.process.quotemonitor.config.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig.class)
public class QuoteMonitorInstrumentQuoteQueryServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_QUOTE_QUERY_SERVICE =
            "quoteMonitorInstrumentQuoteQueryService";

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_QUOTE_QUERY_SERVICE)
    public QuoteMonitorInstrumentQuoteQueryService quoteMonitorInstrumentQuoteQueryService(
            @Qualifier(RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_REGISTRY)
            RuntimeQuoteMonitorInstrumentQuoteRegistry registry
    ) {
        return new QuoteMonitorInstrumentQuoteQueryService(registry);
    }
}
