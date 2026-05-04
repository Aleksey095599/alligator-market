package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.mapper;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.dto.CurrencyListItemResponse;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.Currency;

import java.util.Objects;

/**
 * Маппер доменной валюты в response list query.
 */
public final class CurrencyListItemResponseMapper {

    private CurrencyListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CurrencyListItemResponse toResponse(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        return new CurrencyListItemResponse(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.fractionDigits()
        );
    }
}
