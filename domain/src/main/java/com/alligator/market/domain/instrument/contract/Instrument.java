package com.alligator.market.domain.instrument.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 */
public sealed interface Instrument permits AbstractInstrument {

    /**
     * Внутренний код инструмента.
     */
    String code();

    /**
     * Тип инструмента.
     */
    InstrumentType type();
}
