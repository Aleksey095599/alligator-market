package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;

/**
 * Application-сервис (use case) для операций с инструментами FX_SPOT.
 */
public interface FxSpotUseCase {

    /** Сохранить новый инструмент. */
    String create(FxSpot fxSpot);

    /** Обновить инструмент. */
    void update(FxSpot fxSpot);

    /** Удалить инструмент по коду. */
    void delete(String code);

    /** Вернуть все инструменты. */
    List<FxSpot> findAll();
}
