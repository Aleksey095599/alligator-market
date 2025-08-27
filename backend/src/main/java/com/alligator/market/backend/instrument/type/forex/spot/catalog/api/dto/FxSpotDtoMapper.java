package com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: модель ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface FxSpotDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    @Mapping(target = "baseCurrency", source = "base.code")
    @Mapping(target = "quoteCurrency", source = "quote.code")
    FxSpotDto toDto(FxSpot model);
}

