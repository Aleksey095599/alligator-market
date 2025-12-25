package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in.UpdateCurrencyDto;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Маппер: модель валюты ↔ DTO.
 */
public final class CurrencyDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private CurrencyDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Преобразует основной DTO в доменную модель.
     */
    public static Currency toDomain(CurrencyDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        return new Currency(
                CurrencyCode.of(dto.code()),
                dto.name(),
                dto.country(),
                dto.fractionDigits()
        );
    }

    /**
     * Преобразует DTO обновления и код в доменную модель.
     */
    public static Currency toDomain(String code, UpdateCurrencyDto dto) {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(dto, "dto must not be null");

        return new Currency(
                CurrencyCode.of(code),
                dto.name(),
                dto.country(),
                dto.fractionDigits()
        );
    }

    /**
     * Преобразует доменную модель в основной DTO.
     */
    public static CurrencyDto toDto(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        return new CurrencyDto(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.fractionDigits()
        );
    }
}
