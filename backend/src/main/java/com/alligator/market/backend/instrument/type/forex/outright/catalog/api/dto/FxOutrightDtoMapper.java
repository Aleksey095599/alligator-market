package com.alligator.market.backend.instrument.type.forex.outright.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import org.mapstruct.Mapper;

/**
 * Маппер: FX_OUTRIGHT модель ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface FxOutrightDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    FxOutright toDomain(FxOutrightDto dto);

    /** Преобразует доменную модель в основной DTO. */
    FxOutrightDto toDto(FxOutright model);
}

