package com.alligator.market.domain.instrument.base;

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
