package com.alligator.market.domain.instrument.base.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 */
public interface Instrument {

    /** Возвращает внутренний код инструмента. */
    String code();

    /** Возвращает тип инструмента. */
    InstrumentType type();
}
