package com.alligator.market.backend.instrument.asset.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpot;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

/**
 * Application-сервис (use case) для операций с инструментами FX_SPOT.
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
