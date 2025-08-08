package com.alligator.market.backend.instrument_catalog.currency.service;

import com.alligator.market.domain.instrument.currency.model.Currency;

import java.util.List;

/**
 * Контракт сервиса операций с валютами в хранилище данных.
 */
public interface CurrencyService {

    /** Сохранить новую валюту. */
    String createCurrency(Currency currency);

    /** Обновить существующую валюту. */
    void updateCurrency(Currency currency);

    /** Удалить валюту по коду. */
    void deleteCurrency(String code);

    /** Вернуть все валюты. */
    List<Currency> findAll();
}
