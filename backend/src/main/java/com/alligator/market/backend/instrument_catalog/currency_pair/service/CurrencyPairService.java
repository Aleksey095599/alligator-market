package com.alligator.market.backend.instrument_catalog.currency_pair.service;

import com.alligator.market.domain.instrument.currency_pair.model.CurrencyPair;

import java.util.List;

/**
 * Контракт сервиса операций с валютными парами в хранилище данных.
 */
public interface CurrencyPairService {

    /** Создать валютную пару. */
    String create(CurrencyPair pair);

    /** Обновить валютную пару. */
    void update(CurrencyPair pair);

    /** Удалить пару по коду базовой и котируемой валют. */
    void delete(String base, String quote);

    /** Вернуть все валютные пары. */
    List<CurrencyPair> findAll();
}
