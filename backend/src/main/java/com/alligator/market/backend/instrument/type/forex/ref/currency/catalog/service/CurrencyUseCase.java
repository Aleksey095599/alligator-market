package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;

import java.util.List;

/**
 * Application-сервис (use case) для операций с валютами.
 */
public interface CurrencyUseCase {

    /** Создать новую валюту. */
    String create(Currency currency);

    /** Обновить существующую валюту. */
    void update(Currency currency);

    /** Удалить валюту по коду. */
    void delete(String code);

    /** Вернуть все валюты. */
    List<Currency> getAll();
}
