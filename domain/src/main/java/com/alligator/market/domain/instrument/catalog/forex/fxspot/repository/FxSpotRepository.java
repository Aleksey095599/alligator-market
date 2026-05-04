package com.alligator.market.domain.instrument.catalog.forex.fxspot.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.FxSpot;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий инструментов FOREX_SPOT.
 */
public interface FxSpotRepository {

    /**
     * Создать новый инструмент.
     */
    FxSpot create(FxSpot fxSpot);

    /**
     * Обновить инструмент.
     */
    FxSpot update(FxSpot fxSpot);

    /**
     * Удалить инструмент по коду.
     */
    void deleteByCode(InstrumentCode instrumentCode);

    /**
     * Найти инструмент по коду.
     */
    Optional<FxSpot> findByCode(InstrumentCode instrumentCode);

    /**
     * Вернуть все инструменты.
     */
    List<FxSpot> findAll();
}
