package com.alligator.market.backend.instrument.type.forex.currency.service;

import com.alligator.market.domain.instrument.type.forex.currency.Currency;

import java.util.List;

/**
 * Контракт сервиса операций с валютами в хранилище данных.
 */
public interface CurrencyService {

    String createCurrency(Currency currency);
    void updateCurrency(Currency currency);
    void deleteCurrency(String code);
    List<Currency> findAll();
}
