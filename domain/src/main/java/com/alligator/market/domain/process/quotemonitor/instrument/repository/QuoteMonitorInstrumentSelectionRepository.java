package com.alligator.market.domain.process.quotemonitor.instrument.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;

public interface QuoteMonitorInstrumentSelectionRepository {
    QuoteMonitorInstrumentSelection get();

    boolean addIfAbsent(InstrumentCode instrumentCode);

    boolean removeIfExists(InstrumentCode instrumentCode);

    void replace(QuoteMonitorInstrumentSelection selection);
}
