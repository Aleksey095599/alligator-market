package com.alligator.market.domain.instrument.market.forex.spot.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.market.forex.support.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.market.forex.spot.model.FxSpot;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий инструментов FX_SPOT.
 */
public interface FxSpotRepository {

    /**
     * Создать новый инструмент.
     */
    FxSpot create(FxSpot fxSpot);

    /**
     * Обновить инструмент.
     */
    FxSpot update(FxSpot fxSpot);

    /**
     * Удалить инструмент по коду.
     */
    void deleteByCode(InstrumentCode instrumentCode);

    /**
     * Найти инструмент по коду.
     */
    Optional<FxSpot> findByCode(InstrumentCode instrumentCode);

    /**
     * Вернуть все инструменты.
     */
    List<FxSpot> findAll();

    /**
     * Проверить, используется ли код валюты хотя бы в одном инструменте.
     */
    boolean existsByCurrencyCode(CurrencyCode currencyCode);
}
