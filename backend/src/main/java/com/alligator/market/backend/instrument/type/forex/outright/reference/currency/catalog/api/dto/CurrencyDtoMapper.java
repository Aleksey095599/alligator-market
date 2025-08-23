package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: модель валюты ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface CurrencyDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    Currency toDomain(CurrencyDto dto);

    /** Преобразует DTO обновления и код в доменную модель. */
    @Mapping(target = "code", source = "code")
    Currency toDomain(String code, UpdateCurrencyDto dto);

    /** Преобразует доменную модель в основной DTO. */
    CurrencyDto toDto(Currency currency);
}

