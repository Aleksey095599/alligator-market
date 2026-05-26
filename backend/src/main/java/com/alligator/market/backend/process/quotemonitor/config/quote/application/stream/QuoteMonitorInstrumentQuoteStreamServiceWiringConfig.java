package com.alligator.market.backend.process.quotemonitor.config.quote.application.stream;

import com.alligator.market.backend.process.quotemonitor.application.quote.QuoteMonitorInstrumentQuoteStreamService;
import com.alligator.market.backend.process.quotemonitor.application.quote.QuoteMonitorInstrumentQuoteUpdateStream;
import com.alligator.market.backend.process.quotemonitor.config.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig.class)
public class QuoteMonitorInstrumentQuoteStreamServiceWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_QUOTE_STREAM_SERVICE =
            "quoteMonitorInstrumentQuoteStreamService";

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_QUOTE_STREAM_SERVICE)
    public QuoteMonitorInstrumentQuoteStreamService quoteMonitorInstrumentQuoteStreamService(
            @Qualifier(RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_REGISTRY)
            RuntimeQuoteMonitorInstrumentQuoteRegistry registry,
            @Qualifier(RuntimeQuoteMonitorInstrumentQuoteRegistryWiringConfig
                    .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_QUOTE_UPDATE_STREAM)
            QuoteMonitorInstrumentQuoteUpdateStream updateStream
    ) {
        return new QuoteMonitorInstrumentQuoteStreamService(registry, updateStream);
    }
}
