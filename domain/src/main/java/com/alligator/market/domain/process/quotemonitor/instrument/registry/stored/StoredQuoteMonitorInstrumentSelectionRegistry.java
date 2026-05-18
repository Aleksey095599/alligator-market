package com.alligator.market.domain.process.quotemonitor.instrument.registry.stored;

import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;

public interface StoredQuoteMonitorInstrumentSelectionRegistry {

    QuoteMonitorInstrumentSelection getSelection();
}
