package com.alligator.market.domain.instrument.base;

import com.alligator.market.domain.instrument.base.classification.AssetClass;
import com.alligator.market.domain.instrument.base.classification.ContractType;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.instrument.base.vo.InstrumentSymbol;

/**
 * Базовый финансовый инструмент (доменный контракт).
 *
 * <p>Назначение: Определяет базовый набор атрибутов, общих для всех финансовых инструментов.
 * Специфические характеристики конкретных инструментов задаются через классы-наследники.</p>
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
}
