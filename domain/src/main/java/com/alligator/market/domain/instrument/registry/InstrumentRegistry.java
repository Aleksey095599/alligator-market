package com.alligator.market.domain.instrument.registry;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

public interface InstrumentRegistry {

    boolean contains(InstrumentCode code);

    List<InstrumentCode> registeredCodes();
}
