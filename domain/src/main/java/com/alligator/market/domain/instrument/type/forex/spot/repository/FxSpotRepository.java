package com.alligator.market.domain.instrument.type.forex.spot.repository;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.List;
import java.util.Optional;

/**
 * Порт репозитория инструментов FX_SPOT.
 */
public interface FxSpotRepository {

    /** Сохранить инструмент. */
    void save(FxSpot fxSpot);

    /** Удалить инструмент по коду. */
    void delete(String code);

    /** Найти инструмент по коду. */
    Optional<FxSpot> find(String code);

    /** Вернуть все инструменты. */
    List<FxSpot> findAll();

    /** Проверить, используется ли заданная валюта хотя бы в одном инструменте FX_SPOT. */
    boolean existsByCurrency(Currency currency);
}
