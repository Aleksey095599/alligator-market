package com.alligator.market.domain.instrument.type.forex.currency;

import java.util.List;

/**
 * Интерфейс сервиса для операций с валютами.
 */
public interface CurrencyService {

    String createCurrency(Currency currency);

    void updateCurrency(Currency currency);

    void deleteCurrency(String code);

    List<Currency> findAll();
}
