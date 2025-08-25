package com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.mapstruct.Mapper;

/**
 * Маппер: модель ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface FxSpotDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    FxSpot toDomain(FxSpotDto dto);

    /** Преобразует доменную модель в основной DTO. */
    FxSpotDto toDto(FxSpot model);
}

