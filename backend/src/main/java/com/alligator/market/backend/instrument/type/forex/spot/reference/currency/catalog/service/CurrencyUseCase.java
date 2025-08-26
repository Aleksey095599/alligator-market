package com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;

import java.util.List;

/**
 * Application-сервис (use case) для операций с валютами.
 */
public interface CurrencyUseCase {

    /** Сохранить новую валюту. */
    String createCurrency(Currency currency);

    /** Обновить существующую валюту. */
    void updateCurrency(Currency currency);

    /** Удалить валюту по коду. */
    void deleteCurrency(String code);

    /** Вернуть все валюты. */
    List<Currency> getAll();
}
