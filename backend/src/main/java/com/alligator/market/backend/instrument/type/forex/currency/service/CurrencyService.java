package com.alligator.market.backend.instrument.type.forex.currency.service;

import com.alligator.market.domain.instrument.type.forex.currency.Currency;

import java.util.List;

/**
 * Контракт сервиса операций с валютами в хранилище данных.
 */
public interface CurrencyService {

    /** Создать новую валюту. */
    String createCurrency(Currency currency);

    /** Обновить существующую валюту. */
    void updateCurrency(Currency currency);

    /** Удалить валюту по коду. */
    void deleteCurrency(String code);

    /** Вернуть все валюты. */
    List<Currency> findAll();
}
