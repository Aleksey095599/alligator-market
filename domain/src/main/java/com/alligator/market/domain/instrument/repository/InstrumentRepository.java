package com.alligator.market.domain.instrument.repository;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository<I extends Instrument> {

    Optional<I> findByCode(InstrumentCode instrumentCode);

    List<I> findAll();
}
