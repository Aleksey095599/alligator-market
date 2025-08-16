package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.mapper;

import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.CurrencyUpdateDto;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер между валютой и её DTO.
 */
@Mapper(componentModel = "spring")
public interface CurrencyDtoMapper {

    /** Преобразует DTO в доменную модель. */
    Currency toDomain(CurrencyDto dto);

    /** Преобразует DTO обновления и код в доменную модель. */
    @Mapping(target = "code", source = "code")
    @Mapping(target = "decimal", source = "dto.decimal")
    Currency toDomain(String code, CurrencyUpdateDto dto);

    /** Преобразует доменную модель в DTO. */
    CurrencyDto toDto(Currency currency);
}

