package com.alligator.market.backend.instrument_catalog.currency_pair.service;

import com.alligator.market.domain.instrument.currency_pair.CurrencyPair;

import java.util.List;

/**
 * Контракт сервиса операций с валютными парами в хранилище данных.
 */
public interface CurrencyPairService {

    String create(CurrencyPair pair);
    void update(CurrencyPair pair);
    void delete(String base, String quote);
    List<CurrencyPair> findAll();
}
