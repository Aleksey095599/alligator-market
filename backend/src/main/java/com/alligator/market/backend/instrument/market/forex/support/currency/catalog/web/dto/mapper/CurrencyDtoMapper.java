package com.alligator.market.backend.instrument.market.forex.support.currency.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.market.forex.support.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.market.forex.support.currency.catalog.web.dto.in.CurrencyUpdateDto;
import com.alligator.market.domain.instrument.market.forex.support.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.market.forex.support.currency.model.Currency;

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
     * Общий DTO --> модель.
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
     * Код валюты + DTO обновления --> модель.
     */
    public static Currency toDomainUpdate(String code, CurrencyUpdateDto dto) {
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
     * Модель --> общий DTO.
     */
    public static CurrencyDto toCurrencyDto(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        return new CurrencyDto(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.fractionDigits()
        );
    }
}
