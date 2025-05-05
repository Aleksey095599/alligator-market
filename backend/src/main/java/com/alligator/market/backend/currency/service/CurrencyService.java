package com.alligator.market.backend.currency.service;

import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.entity.Currency;

public interface CurrencyService {
    Currency create(CreateCurrencyRequest dto);
}
