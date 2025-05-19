package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CurrencyDto;

/* Интерфейс сервиса для операций с валютами. */
public interface CurrencyService {

    String createCurrency(CurrencyDto dto);

    void deleteCurrency(String code);
}
