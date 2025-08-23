package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.service.crud;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;

import java.util.List;

/**
 * Сервисный контракт, используемый REST-слоем для работы с валютами:
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
