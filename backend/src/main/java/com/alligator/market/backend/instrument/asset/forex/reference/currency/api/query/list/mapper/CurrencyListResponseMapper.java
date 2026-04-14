package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.mapper;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.dto.CurrencyListResponse;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;

import java.util.Objects;

/* Маппер доменной валюты в response list query. */
public final class CurrencyListResponseMapper {

    private CurrencyListResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CurrencyListResponse toResponse(Currency currency) {
        // Проверяем входной аргумент на null.
        Objects.requireNonNull(currency, "currency must not be null");

        // Маппим доменную модель в локальный response DTO.
        return new CurrencyListResponse(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.fractionDigits()
        );
    }
}
