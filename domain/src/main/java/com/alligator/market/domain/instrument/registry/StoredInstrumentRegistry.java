package com.alligator.market.domain.instrument.registry;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

public interface StoredInstrumentRegistry {

    boolean contains(InstrumentCode code);

    List<InstrumentCode> registeredCodes();
}
