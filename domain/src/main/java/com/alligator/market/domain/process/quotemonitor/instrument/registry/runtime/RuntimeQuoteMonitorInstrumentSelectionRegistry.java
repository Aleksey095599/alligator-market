package com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;

import java.util.List;

public interface RuntimeQuoteMonitorInstrumentSelectionRegistry {

    QuoteMonitorInstrumentSelection currentSelection();

    List<InstrumentCode> selectedInstrumentCodes();

    boolean isSelected(InstrumentCode instrumentCode);
}
