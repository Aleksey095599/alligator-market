package com.alligator.market.backend.process.quotemonitor.application.quote;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistry;

import java.util.List;
import java.util.Objects;

public final class QuoteMonitorInstrumentQuoteQueryService {
    private final RuntimeQuoteMonitorInstrumentQuoteRegistry registry;

    public QuoteMonitorInstrumentQuoteQueryService(RuntimeQuoteMonitorInstrumentQuoteRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    public List<QuoteMonitorInstrumentQuote> currentQuotes() {
        return registry.currentQuotes();
    }
}
