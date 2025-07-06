package com.alligator.market.domain.instrument.forex.currency;

import java.util.List;

/**
 * Интерфейс сервиса для операций с валютами.
 */
public interface CurrencyService {

    String createCurrency(Currency currency);

    void updateCurrency(String code, Currency currency);

    void deleteCurrency(String code);

    List<Currency> findAll();
}
