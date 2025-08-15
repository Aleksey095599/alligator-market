package com.alligator.market.domain.instrument.type.forex.outright.catalog;

import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище инструментов FX_OUTRIGHT.
 */
public interface FxOutrightStorage {

    /** Сохранить инструмент FX OUTRIGHT. */
    void save(FxOutright instrument);

    /** Удалить инструмент по внутреннему коду. */
    void delete(String internalCode);

    /** Найти инструмент по внутреннему коду. */
    Optional<FxOutright> find(String internalCode);

    /** Вернуть все инструменты. */
    List<FxOutright> findAll();
}
