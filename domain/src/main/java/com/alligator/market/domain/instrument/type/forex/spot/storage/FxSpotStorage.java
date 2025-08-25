package com.alligator.market.domain.instrument.type.forex.spot.storage;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище инструментов FX_SPOT.
 */
public interface FxSpotStorage {

    /** Сохранить инструмент. */
    void save(FxSpot instrument);

    /** Удалить инструмент по коду. */
    void delete(String code);

    /** Найти инструмент по коду. */
    Optional<FxSpot> find(String code);

    /** Вернуть все инструменты. */
    List<FxSpot> findAll();

    /** Проверить, используется ли валюта в инструментах. */
    boolean existsByCurrency(String currencyCode);
}
