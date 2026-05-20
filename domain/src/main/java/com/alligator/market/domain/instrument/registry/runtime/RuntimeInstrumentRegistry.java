package com.alligator.market.domain.instrument.registry.runtime;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Map;
import java.util.Optional;

public interface RuntimeInstrumentRegistry {

    Optional<Instrument> findByCode(InstrumentCode instrumentCode);

    Map<InstrumentCode, Instrument> instrumentsByCode();
}
