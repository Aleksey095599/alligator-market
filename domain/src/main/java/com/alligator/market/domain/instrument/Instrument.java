package com.alligator.market.domain.instrument;

import com.alligator.market.domain.instrument.type.AssetClass;
import com.alligator.market.domain.instrument.type.ContractType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;

/**
 * Базовый контракт финансового инструмента.
 *
 * <p>Назначение: Определяет базовый набор атрибутов, общих для всех финансовых инструментов.
 * Специфические характеристики конкретных инструментов задаются в классах наследниках.</p>
 */
public interface Instrument {

    /**
     * Уникальный код инструмента (идентификатор).
     */
    InstrumentCode instrumentCode();

    /**
     * Символ инструмента: для отображения в UI (более дружелюбен чем код инструмента).
     */
    InstrumentSymbol instrumentSymbol();

    /**
     * Класс актива, к которому относится финансовый инструмент.
     */
    AssetClass assetClass();

    /**
     * Тип контракта финансового инструмента.
     */
    ContractType contractType();

    /**
     * Тип финансового инструмента.
     */
    InstrumentType instrumentType();
}
