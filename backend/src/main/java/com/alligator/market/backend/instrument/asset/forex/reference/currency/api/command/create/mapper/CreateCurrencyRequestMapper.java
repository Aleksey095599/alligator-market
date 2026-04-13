package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.mapper;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.dto.CreateCurrencyRequest;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Локальный маппер create-slice для преобразования transport-request в доменную модель.
 */
public final class CreateCurrencyRequestMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private CreateCurrencyRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create-request --> доменная модель валюты.
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
