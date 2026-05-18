package com.alligator.market.domain.process.quotemonitor.instrument.registry.sync;

import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;

public interface RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher {

    void replaceWith(RuntimeQuoteMonitorInstrumentSelectionRegistry registry);
}
