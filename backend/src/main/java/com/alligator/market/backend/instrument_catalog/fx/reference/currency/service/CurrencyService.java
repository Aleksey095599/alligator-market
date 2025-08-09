package com.alligator.market.backend.instrument_catalog.fx.reference.currency.service;

import com.alligator.market.domain.instrument.type.fx.reference.currency.model.Currency;

import java.util.List;

/**
 * Сервис работы с валютами.
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
