package com.alligator.market.domain.instrument;

import com.alligator.market.domain.instrument.classification.Asset;
import com.alligator.market.domain.instrument.classification.ContractType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;

/**
 * Доменный контракт финансового инструмента.
 *
 * <p>Назначение: Определяет обязательные атрибуты, общие для всех финансовых инструментов.
 * Специфические характеристики конкретных инструментов задаются в их доменных моделях.</p>
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
     * Актив финансового инструмента.
     */
    Asset asset();

    /**
     * Контракт финансового инструмента.
     */
    ContractType contractType();
}
