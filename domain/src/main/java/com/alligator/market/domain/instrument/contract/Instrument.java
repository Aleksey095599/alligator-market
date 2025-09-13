package com.alligator.market.domain.instrument.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 */
public interface Instrument {

    /** Внутренний код инструмента. */
    String code();

    /** Тип инструмента. */
    InstrumentType type();
}
