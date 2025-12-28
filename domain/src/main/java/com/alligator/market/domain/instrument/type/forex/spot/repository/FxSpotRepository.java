package com.alligator.market.domain.instrument.type.forex.spot.repository;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Порт репозитория инструментов FX_SPOT.
 */
public interface FxSpotRepository {

    /**
     * Создать новый инструмент.
     */
    FxSpot create(FxSpot fxSpot);

    /**
     * Обновить существующий инструмент.
     */
    FxSpot update(FxSpot fxSpot);

    /**
     * Удалить инструмент по коду.
     */
    void deleteByCode(String instrumentCode);

    /**
     * Удалить инструмент по коду.
     */
    default void deleteByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        deleteByCode(instrumentCode.value());
    }

    /**
     * Найти инструмент по коду.
     */
    Optional<FxSpot> findByCode(String instrumentCode);

    /**
     * Найти инструмент по коду.
     */
    default Optional<FxSpot> findByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return findByCode(instrumentCode.value());
    }

    /**
     * Вернуть все инструменты.
     */
    List<FxSpot> findAll();

    /**
     * Проверить, используется ли код валюты хотя бы в одном инструменте.
     */
    boolean existsByCurrencyCode(CurrencyCode currencyCode);
}
