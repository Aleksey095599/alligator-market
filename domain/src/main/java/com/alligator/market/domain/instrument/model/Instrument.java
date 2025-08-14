package com.alligator.market.domain.instrument.model;

/**
 * Единый контракт финансового инструмента.
 */
public interface Instrument {

    /** Внутренний код инструмента. */
    String code();

    /** Тип финансового инструмента. */
    InstrumentType type();
}
