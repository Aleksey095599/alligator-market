package com.alligator.market.domain.instrument.type.fx.outright.catalog;

import com.alligator.market.domain.instrument.type.fx.outright.model.FxOutright;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище инструментов FX SPOT.
 */
public interface FxOutrightStorage {

    /** Сохранить инструмент FX SPOT. */
    void save(FxOutright instrument);

    /** Удалить все инструменты по коду валютной пары. */
    void delete(String pairCode);

    /** Найти инструмент по внутреннему коду. */
    Optional<FxOutright> find(String internalCode);

    /** Вернуть все инструменты. */
    List<FxOutright> findAll();
}
