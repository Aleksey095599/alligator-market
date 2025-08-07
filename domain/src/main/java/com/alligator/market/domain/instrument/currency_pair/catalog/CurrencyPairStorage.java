package com.alligator.market.domain.instrument.currency_pair.catalog;

import com.alligator.market.domain.instrument.currency_pair.model.CurrencyPair;

import java.util.List;
import java.util.Optional;

/**
 * Контракт хранилища валютных пар.
 */
public interface CurrencyPairStorage {

    String save(CurrencyPair pair);

    void delete(String base, String quote);

    Optional<CurrencyPair> find(String base, String quote);

    /** Существует ли хотя бы одна валютная пара, использующая данную валюту. */
    boolean existsByCurrency(String currencyCode);

    List<CurrencyPair> findAll();
}
