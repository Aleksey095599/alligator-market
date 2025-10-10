package com.alligator.market.domain.instrument.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 */
public interface Instrument {

    /** Внутренний код инструмента (уникален в контексте приложения). */
    String code();

    /** Символ инструмента для отображения в UI. */
    String symbol();

    /** Тип инструмента. */
    InstrumentType type();
}
