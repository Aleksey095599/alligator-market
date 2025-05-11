package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;

/* Интерфейс сервиса для операций с валютами. */
public interface CurrencyService {

    String createCurrency(CreateCurrencyRequest dto);

    void deleteCurrency(String code);
}
