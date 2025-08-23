package com.alligator.market.domain.instrument.contract;

/**
 * Единый контракт финансового инструмента.
 */
public interface Instrument {

    /** Возвращает внутренний код инструмента. */
    String getCode();

    /** Возвращает тип инструмента. */
    InstrumentType getType();
}
