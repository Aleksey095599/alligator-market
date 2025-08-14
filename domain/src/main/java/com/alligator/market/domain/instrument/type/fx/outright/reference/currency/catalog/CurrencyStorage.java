package com.alligator.market.domain.instrument.type.fx.outright.reference.currency.catalog;

import com.alligator.market.domain.instrument.type.fx.outright.reference.currency.model.Currency;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище валют.
 */
public interface CurrencyStorage {

    /** Сохранить валюту. */
    String save(Currency currency);

    /** Удалить валюту по коду. */
    void deleteByCode(String code);

    /** Найти валюту по коду. */
    Optional<Currency> findByCode(String code);

    /** Найти валюту по имени. */
    Optional<Currency> findByName(String name);

    /** Найти валюту по стране обращения. */
    Optional<Currency> findByCountry(String country);

    /** Вернуть все валюты. */
    List<Currency> findAll();
}
