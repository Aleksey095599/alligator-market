package com.alligator.market.backend.instrument_catalog.fx.spot.service;

import com.alligator.market.domain.instrument.type.fx.spot.model.FxSpot;

import java.util.List;

/**
 * Сервис работы с инструментами FX SPOT.
 */
public interface FxSpotInstrumentService {

    /** Создает записи для указанной валютной пары. */
    void createForPair(String pairCode);

    /** Удаляет записи для указанной валютной пары. */
    void deleteForPair(String pairCode);

    /** Вернуть все инструменты. */
    List<FxSpot> findAll();
}
