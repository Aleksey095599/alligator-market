package com.alligator.market.domain.instrument.model;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.asset.InstrumentType;

/**
 * Базовый контракт финансового инструмента.
 *
 * <p>Назначение: Определяет базовый набор атрибутов, общих для всех финансовых инструментов.
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
    InstrumentSymbol instrumentSymbol();

    /**
     * Тип финансового инструмента.
     */
    InstrumentType instrumentType();
}
