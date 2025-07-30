package com.alligator.market.domain.instrument;

/**
 * Единый контракт базового финансового инструмента.
 */
public interface Instrument {

    /** Биржевой идентификатор инструмента. */
    String symbol();

    /** Тип финансового инструмента. */
    InstrumentType instrumentType();
}
