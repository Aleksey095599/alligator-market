package com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;

import java.util.List;

/**
 * Сервисный контракт, используемый REST-контроллером для работы с валютами:
 * создание, обновление, удаление и получение списка и т.п.
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
