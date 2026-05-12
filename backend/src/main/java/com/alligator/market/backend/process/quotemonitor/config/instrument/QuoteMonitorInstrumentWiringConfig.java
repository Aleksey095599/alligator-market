package com.alligator.market.backend.process.quotemonitor.config.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.QuoteMonitorInstrumentService;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentPort;
import com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument.JooqQuoteMonitorInstrumentAdapter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class QuoteMonitorInstrumentWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_PORT =
            "quoteMonitorInstrumentPort";
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_SERVICE =
            "quoteMonitorInstrumentService";

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_PORT)
    public QuoteMonitorInstrumentPort quoteMonitorInstrumentPort(DSLContext dsl) {
        return new JooqQuoteMonitorInstrumentAdapter(dsl);
    }

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_SERVICE)
    public QuoteMonitorInstrumentService quoteMonitorInstrumentService(
            @Qualifier(BEAN_QUOTE_MONITOR_INSTRUMENT_PORT)
            QuoteMonitorInstrumentPort port
    ) {
        return new QuoteMonitorInstrumentService(port);
    }
}
