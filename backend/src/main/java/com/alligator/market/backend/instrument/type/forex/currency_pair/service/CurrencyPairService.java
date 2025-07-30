package com.alligator.market.backend.instrument.type.forex.currency_pair.service;

import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;

import java.util.List;

/**
 * Контракт сервиса операций с валютными парами в хранилище данных.
 */
public interface CurrencyPairService {

    /** Создать новую пару. */
    String create(CurrencyPair pair);

    /** Обновить существующую пару. */
    void update(CurrencyPair pair);

    /** Удалить пару базовой и котируемой валюте. */
    void delete(String base, String quote);

    /** Вернуть все пары. */
    List<CurrencyPair> findAll();
}
