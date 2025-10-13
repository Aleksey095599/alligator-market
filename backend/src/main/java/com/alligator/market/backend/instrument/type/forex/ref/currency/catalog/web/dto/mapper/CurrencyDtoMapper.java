package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.in.UpdateCurrencyDto;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Маппер: модель валюты ⇄ DTO.
 */
@Component
public class CurrencyDtoMapper {

    /** Приватный конструктор (запрещает создание экземпляров). */
    private CurrencyDtoMapper() { throw new UnsupportedOperationException("Utility class"); }

    /** Преобразует основной DTO в доменную модель. */
    public Currency toDomain(CurrencyDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        return new Currency(
                CurrencyCode.of(dto.code()),
                dto.name(),
                dto.country(),
                dto.fractionDigits()
        );
    }

    /** Преобразует DTO обновления и код в доменную модель. */
    public Currency toDomain(String code, UpdateCurrencyDto dto) {
        Objects.requireNonNull(code, "code must not be null");

        return new Currency(
                CurrencyCode.of(code),
                dto.name(),
                dto.country(),
                dto.fractionDigits()
        );
    }

    /** Преобразует доменную модель в основной DTO. */
    public CurrencyDto toDto(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        return new CurrencyDto(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.fractionDigits()
        );
    }
}
