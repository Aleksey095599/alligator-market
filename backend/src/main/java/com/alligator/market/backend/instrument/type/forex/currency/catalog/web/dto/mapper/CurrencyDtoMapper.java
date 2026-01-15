package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in.CurrencyUpdateDto;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.code.CurrencyCode;

import java.util.Objects;

/**
 * Маппер DTO для валюты.
 */
public final class CurrencyDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private CurrencyDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Преобразует общий DTO {@link CurrencyDto} в доменную модель {@link Currency}.
     */
    public static Currency toDomain(CurrencyDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        return new Currency(
                CurrencyCode.of(dto.getCode()),
                dto.getName(),
                dto.getCountry(),
                dto.getFractionDigits()
        );
    }

    /**
     * Преобразует код и DTO обновления {@link CurrencyUpdateDto} в доменную модель {@link Currency}.
     */
    public static Currency toDomain(String code, CurrencyUpdateDto dto) {
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
     * Преобразует доменную модель {@link Currency} в DTO ответа {@link CurrencyDto}.
     */
    public static CurrencyDto toResponseDto(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        return new CurrencyDto(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.fractionDigits()
        );
    }
}
