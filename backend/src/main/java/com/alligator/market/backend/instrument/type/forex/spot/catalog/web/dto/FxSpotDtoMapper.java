package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.springframework.stereotype.Component;

/**
 * Маппер: модель ⇄ DTO.
 */
@Component
public class FxSpotDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    public FxSpotDto toDto(FxSpot model) {
        return new FxSpotDto(
                model.base().code(),
                model.quote().code(),
                model.quoteDecimal(),
                model.valueDateCode()
        );
    }
}

