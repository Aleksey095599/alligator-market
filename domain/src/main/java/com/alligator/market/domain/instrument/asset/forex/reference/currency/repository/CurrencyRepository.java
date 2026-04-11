package com.alligator.market.domain.instrument.asset.forex.reference.currency.repository;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий валют.
 */
public interface CurrencyRepository {

    /**
     * Создать новую валюту.
     */
    Currency create(Currency currency);

    /**
     * Обновить валюту.
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
