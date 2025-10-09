package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.List;

/**
 * Application-сервис (use case) для операций с валютами.
 */
public interface CurrencyUseCase {

    /** Создать новую валюту. */
    Currency create(Currency currency);

    /** Обновить существующую валюту. */
    Currency update(Currency currency);

    /** Удалить валюту по коду. */
    void delete(CurrencyCode code);

    /** Вернуть все валюты. */
    List<Currency> getAll();
}
