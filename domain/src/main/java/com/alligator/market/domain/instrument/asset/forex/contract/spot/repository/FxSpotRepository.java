package com.alligator.market.domain.instrument.asset.forex.contract.spot.repository;

import com.alligator.market.domain.instrument.model.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.InstrumentFxSpot;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий инструментов FOREX_SPOT.
 */
public interface FxSpotRepository {

    /**
     * Создать новый инструмент.
     */
    InstrumentFxSpot create(InstrumentFxSpot fxSpot);

    /**
     * Обновить инструмент.
     */
    InstrumentFxSpot update(InstrumentFxSpot fxSpot);

    /**
     * Удалить инструмент по коду.
     */
    void deleteByCode(InstrumentCode instrumentCode);

    /**
     * Найти инструмент по коду.
     */
    Optional<InstrumentFxSpot> findByCode(InstrumentCode instrumentCode);

    /**
     * Вернуть все инструменты.
     */
    List<InstrumentFxSpot> findAll();

    /**
     * Проверить, используется ли код валюты хотя бы в одном инструменте.
     */
    boolean existsByCurrencyCode(CurrencyCode currencyCode);
}
