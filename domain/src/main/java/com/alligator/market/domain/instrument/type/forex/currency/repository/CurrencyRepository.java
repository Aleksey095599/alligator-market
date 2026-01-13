package com.alligator.market.domain.instrument.type.forex.currency.repository;

import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.code.CurrencyCode;

import java.util.List;
import java.util.Optional;

/**
 * Доменный репозиторий валют.
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
