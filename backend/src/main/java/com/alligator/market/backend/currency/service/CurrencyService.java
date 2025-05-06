package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;

/* Интерфейс сервиса для операций с валютами. */
public interface CurrencyService {

    // Метод создания новой валюты
    Currency create(CreateCurrencyRequest dto);

}
