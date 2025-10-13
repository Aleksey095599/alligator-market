package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;

/**
 * Application-сервис (use case) для операций с инструментами FX_SPOT.
 */
public interface FxSpotUseCase {

    /** Создать новый FX_SPOT инструмент. */
    FxSpot create(FxSpot fxSpot);

    /** Обновить существующий FX_SPOT инструмент. */
    FxSpot update(FxSpot fxSpot);

    /** Удалить инструмент FX_SPOT по коду. */
    void delete(String instrumentCode);

    /** Вернуть все инструменты FX_SPOT. */
    List<FxSpot> findAll();
}
