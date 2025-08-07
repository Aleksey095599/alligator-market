package com.alligator.market.domain.instrument.currency.catalog;

import com.alligator.market.domain.instrument.currency.Currency;

import java.util.List;
import java.util.Optional;

/**
 * Контракт хранилища валют.
 */
public interface CurrencyStorage {

    String save(Currency currency);
    void deleteByCode(String code);
    Optional<Currency> findByCode(String code);
    Optional<Currency> findByName(String name);
    Optional<Currency> findByCountry(String country);
    List<Currency> findAll();
}
