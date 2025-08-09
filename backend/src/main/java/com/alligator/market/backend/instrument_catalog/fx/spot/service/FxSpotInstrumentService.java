package com.alligator.market.backend.instrument_catalog.fx.spot.service;

/**
 * Сервис работы с инструментами FX SPOT.
 */
public interface FxSpotInstrumentService {

    /** Создает записи для указанной валютной пары. */
    void createForPair(String pairCode);

    /** Удаляет записи для указанной валютной пары. */
    void deleteForPair(String pairCode);
}
