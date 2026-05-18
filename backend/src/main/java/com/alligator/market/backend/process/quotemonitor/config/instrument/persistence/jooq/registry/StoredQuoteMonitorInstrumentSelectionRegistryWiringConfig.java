package com.alligator.market.backend.process.quotemonitor.config.instrument.persistence.jooq.registry;

import com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument.JooqStoredQuoteMonitorInstrumentSelectionRegistryAdapter;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.stored.StoredQuoteMonitorInstrumentSelectionRegistry;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StoredQuoteMonitorInstrumentSelectionRegistryWiringConfig {
    public static final String BEAN_STORED_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY =
            "storedQuoteMonitorInstrumentSelectionRegistry";

    @Bean(BEAN_STORED_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY)
    public StoredQuoteMonitorInstrumentSelectionRegistry storedQuoteMonitorInstrumentSelectionRegistry(
            DSLContext dsl
    ) {
        return new JooqStoredQuoteMonitorInstrumentSelectionRegistryAdapter(dsl);
    }
}
