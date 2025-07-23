package com.alligator.market.backend.instrument.type.forex.currency_pair.service;

import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;

import java.util.List;

/**
 * Сервис для операций с валютными парами в таблице БД.
 */
public interface CurrencyPairService {

    /** Создать новую пару */
    String createPair(CurrencyPair pair);

    /** Обновить существующую пару */
    void updatePair(CurrencyPair pair);

    /** Удалить пару по коду */
    void deletePair(String pair);

    /** Вернуть все пары */
    List<CurrencyPair> findAll();
}
