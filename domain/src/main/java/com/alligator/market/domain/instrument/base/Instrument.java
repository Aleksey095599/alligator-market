package com.alligator.market.domain.instrument.base;

/**
 * Базовый контракт финансового инструмента.
 */
public interface Instrument {

    /** Возвращает внутренний код инструмента. */
    String getCode();
}
