package com.alligator.market.domain.instrument.type.forex.spot.reference.currency.storage;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;

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

    /** Вернуть все валюты. */
    List<Currency> findAll();
}
