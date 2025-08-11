package com.alligator.market.domain.instrument.type.fx.spot.catalog;

import com.alligator.market.domain.instrument.type.fx.spot.model.FxSpot;

import java.util.Optional;

/**
 * Хранилище инструментов FX SPOT.
 */
public interface FxSpotInstrumentStorage {

    /** Сохранить инструмент FX SPOT. */
    String save(FxSpot instrument);

    /** Удалить все инструменты по коду валютной пары. */
    void delete(String pairCode);

    /** Найти инструмент по внутреннему коду. */
    Optional<FxSpot> find(String internalCode);
}
