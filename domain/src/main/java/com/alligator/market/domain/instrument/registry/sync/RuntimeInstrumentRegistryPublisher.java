package com.alligator.market.domain.instrument.registry.sync;

import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;

public interface RuntimeInstrumentRegistryPublisher {

    void replaceWith(RuntimeInstrumentRegistry registry);
}
