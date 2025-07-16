package com.alligator.market.domain.instrument;

/**
 * Контракт базового финансового инструмента.
 */
public interface Instrument {

    /* Биржевой идентификатор инструмента */
    String symbol();
}
