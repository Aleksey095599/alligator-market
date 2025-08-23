package com.alligator.market.domain.instrument.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Единый контракт финансового инструмента.
 */
public interface Instrument {

    /** Возвращает внутренний код инструмента. */
    String getCode();

    /** Возвращает тип инструмента. */
    InstrumentType getType();
}
