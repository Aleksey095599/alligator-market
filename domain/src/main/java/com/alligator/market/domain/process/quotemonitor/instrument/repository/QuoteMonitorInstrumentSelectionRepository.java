package com.alligator.market.domain.process.quotemonitor.instrument.repository;

import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;

public interface QuoteMonitorInstrumentSelectionRepository {
    QuoteMonitorInstrumentSelection get();

    void replace(QuoteMonitorInstrumentSelection selection);
}
