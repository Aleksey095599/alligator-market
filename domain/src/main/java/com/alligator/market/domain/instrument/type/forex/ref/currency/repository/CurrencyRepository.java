package com.alligator.market.domain.instrument.type.forex.ref.currency.repository;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.List;
import java.util.Optional;

/**
 * Порт репозитория валют.
 */
public interface CurrencyRepository {

    /**
     * Создать новую валюту.
     */
    Currency create(Currency currency);

    /**
     * Обновить существующую валюту.
     */
    Currency update(Currency currency);

    /**
     * Удалить валюту по коду.
     */
    void deleteByCode(CurrencyCode code);

    /**
     * Найти валюту по коду.
     */
    Optional<Currency> findByCode(CurrencyCode code);

    /**
     * Вернуть все валюты.
     */
    List<Currency> findAll();
}
