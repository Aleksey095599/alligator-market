package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.mapper;

import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.UpdateCurrencyDto;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер между доменной моделью валюты и её DTO.
 */
@Mapper(componentModel = "spring")
public interface CurrencyDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    Currency toDomain(CurrencyDto dto);

    /** Преобразует DTO обновления и код в доменную модель. */
    @Mapping(target = "code", source = "code")
    @Mapping(target = "decimal", source = "dto.decimal")
    Currency toDomain(String code, UpdateCurrencyDto dto);

    /** Преобразует доменную модель в основной DTO. */
    CurrencyDto toDto(Currency currency);
}

