package com.alligator.market.domain.instrument.base.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 */
public interface Instrument {

    /** Возвращает внутренний код инструмента. */
    String getCode();

    /** Возвращает тип инструмента. */
    InstrumentType getType();
}
// TODO: подумать над доменной моделью классом InstrumentBase
