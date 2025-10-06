package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import org.springframework.stereotype.Component;

/**
 * Маппер: модель валюты ⇄ DTO.
 */
@Component
public class CurrencyDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    public Currency toDomain(CurrencyDto dto) {
        return new Currency(
                CurrencyCode.of(dto.code()),
                dto.name(),
                dto.country(),
                dto.decimal()
        );
    }

    /** Преобразует DTO обновления и код в доменную модель. */
    public Currency toDomain(String code, UpdateCurrencyDto dto) {
        return new Currency(
                CurrencyCode.of(code),
                dto.name(),
                dto.country(),
                dto.decimal()
        );
    }

    /** Преобразует доменную модель в основной DTO. */
    public CurrencyDto toDto(Currency currency) {
        return new CurrencyDto(
                currency.code().value(),
                currency.name(),
                currency.country(),
                currency.decimal()
        );
    }
}
