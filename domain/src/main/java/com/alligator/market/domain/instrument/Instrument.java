package com.alligator.market.domain.instrument;

/**
 * Единый контракт финансового инструмента.
 */
public interface Instrument {

    /** Внутренний идентификатор инструмента. */
    String internalCode();

    /** Тип финансового инструмента. */
    InstrumentType instrumentType();
}
