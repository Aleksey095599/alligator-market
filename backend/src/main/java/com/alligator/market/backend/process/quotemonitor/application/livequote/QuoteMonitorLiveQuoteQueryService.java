package com.alligator.market.backend.process.quotemonitor.application.livequote;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistry;

import java.util.List;
import java.util.Objects;

public final class QuoteMonitorLiveQuoteQueryService {
    private final RuntimeQuoteMonitorLiveQuoteRegistry registry;

    public QuoteMonitorLiveQuoteQueryService(RuntimeQuoteMonitorLiveQuoteRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    public List<QuoteMonitorInstrumentQuote> currentQuotes() {
        return registry.currentQuotes();
    }
}
