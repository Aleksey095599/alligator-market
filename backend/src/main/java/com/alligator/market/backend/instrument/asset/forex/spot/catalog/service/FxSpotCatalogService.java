package com.alligator.market.backend.instrument.asset.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.asset.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;

import java.util.List;

/**
 * Application-сервис (use case) для операций с инструментами FOREX_SPOT.
 */
public interface FxSpotCatalogService {

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
    void delete(InstrumentCode instrumentCode);

    /**
     * Вернуть все инструменты.
     */
    List<FxSpot> findAll();
}
