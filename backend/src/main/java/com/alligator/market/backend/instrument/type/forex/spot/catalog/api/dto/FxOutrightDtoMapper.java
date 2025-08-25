package com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.mapstruct.Mapper;

/**
 * Маппер: FX_OUTRIGHT модель ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface FxOutrightDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    FxSpot toDomain(FxOutrightDto dto);

    /** Преобразует доменную модель в основной DTO. */
    FxOutrightDto toDto(FxSpot model);
}

