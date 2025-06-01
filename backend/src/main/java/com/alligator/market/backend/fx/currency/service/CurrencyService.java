package com.alligator.market.backend.fx.currency.service;

import com.alligator.market.backend.fx.currency.dto.CurrencyDto;
import com.alligator.market.backend.fx.currency.dto.CurrencyUpdateDto;

import java.util.List;

/* Интерфейс сервиса для операций с валютами. */
public interface CurrencyService {

    String createCurrency(CurrencyDto dto);

    void updateCurrency(String code, CurrencyUpdateDto dto);

    void deleteCurrency(String code);

    List<CurrencyDto> findAll();
}
