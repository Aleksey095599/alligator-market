package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;

/**
 * Application-сервис (use case) для операций с инструментами FX_SPOT.
 */
public interface FxSpotUseCase {

    /**
     * Создать новый инструмент.
     */
    FxSpot create(FxSpot fxSpot);

    /**
     * Обновить существующий инструмент.
     */
    void update(FxSpot fxSpot);

    /**
     * Удалить инструмент по коду.
     */
    void delete(String instrumentCode);

    /**
     * Вернуть все инструменты.
     */
    List<FxSpot> findAll();
}
