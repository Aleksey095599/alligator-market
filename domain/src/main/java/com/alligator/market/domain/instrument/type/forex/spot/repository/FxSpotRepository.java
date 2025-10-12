package com.alligator.market.domain.instrument.type.forex.spot.repository;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;
import java.util.Optional;

/**
 * Порт репозитория инструментов FX_SPOT.
 */
public interface FxSpotRepository {

    /** Создать новый FX_SPOT инструмент. */
    FxSpot create(FxSpot fxSpot);

    /** Обновить существующий FX_SPOT инструмент. */
    FxSpot update(FxSpot fxSpot);

    /** Удалить инструмент FX_SPOT по коду. */
    void deleteByCode(String instrumentCode);

    /** Найти инструмент FX_SPOT по коду. */
    Optional<FxSpot> findByCode(String instrumentCode);

    /** Вернуть все инструменты FX_SPOT. */
    List<FxSpot> findAll();

    /** Проверить, используется ли заданный код валюты хотя бы в одном инструменте. */
    boolean existsByCurrencyCode(CurrencyCode currencyCode);
}
