package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.mapper;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.dto.CreateCurrencyRequest;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Маппер {@link CreateCurrencyRequest} и доменной модели валюты.
 */
public final class CreateCurrencyRequestMapper {

    private CreateCurrencyRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * {@link CreateCurrencyRequest} --> доменная модель.
     */
    public static Currency toDomain(CreateCurrencyRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        return new Currency(
                CurrencyCode.of(request.code()),
                request.name(),
                request.country(),
                request.fractionDigits()
        );
    }
}
