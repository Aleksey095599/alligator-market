package com.alligator.market.domain.instrument.contract;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 *
 * <p>Определяет базовый набор атрибутов, общих для всех финансовых инструментов.
 * Специфические характеристики конкретных инструментов задаются в классах наследниках.</p>
 */
public interface Instrument {

    /**
     * Код инструмента.
     */
    InstrumentCode instrumentCode();

    /**
     * Символ инструмента: для отображения в UI (более дружелюбен чем код инструмента).
     */
    String instrumentSymbol();

    /**
     * Тип финансового инструмента.
     */
    InstrumentType instrumentType();
}
