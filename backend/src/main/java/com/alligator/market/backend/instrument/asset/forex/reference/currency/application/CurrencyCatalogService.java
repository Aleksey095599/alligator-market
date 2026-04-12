package com.alligator.market.backend.instrument.asset.forex.reference.currency.application;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.List;

/**
 * Application-сервис (use case) для операций с валютами.
 */
public interface CurrencyCatalogService {

    /**
     * Создать новую валюту.
     */
    Currency create(Currency currency);

    /**
     * Обновить существующую валюту.
     */
    void update(Currency currency);

    /**
     * Удалить валюту по коду.
     */
    void delete(CurrencyCode code);

    /**
     * Вернуть все валюты.
     */
    List<Currency> findAll();
}
