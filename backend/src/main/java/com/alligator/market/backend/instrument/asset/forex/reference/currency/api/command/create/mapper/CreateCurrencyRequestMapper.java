package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.mapper;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.dto.CreateCurrencyRequest;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Маппер преобразования Request DTO в доменную модель валюты.
 */
public final class CreateCurrencyRequestMapper {

    private CreateCurrencyRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Request DTO --> доменная модель.
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
