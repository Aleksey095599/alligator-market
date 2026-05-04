package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.mapper;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.dto.UpdateCurrencyRequest;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Маппер {@link UpdateCurrencyRequest} и доменной модели валюты.
 */
public final class UpdateCurrencyRequestMapper {

    private UpdateCurrencyRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * {@link UpdateCurrencyRequest} --> доменная модель.
     */
    public static Currency toDomain(String code, UpdateCurrencyRequest request) {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(request, "request must not be null");

        return new Currency(
                CurrencyCode.of(code),
                request.name(),
                request.country(),
                request.fractionDigits()
        );
    }
}
