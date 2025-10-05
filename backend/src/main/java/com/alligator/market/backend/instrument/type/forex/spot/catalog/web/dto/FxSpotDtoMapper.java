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

    /** Преобразует доменную модель в DTO элемента списка. */
    public FxSpotListItemDto toListItemDto(FxSpot model) {
        return new FxSpotListItemDto(
                model.code(),
                model.symbol(),
                model.base().code(),
                model.quote().code(),
                model.quoteDecimal(),
                model.valueDateCode()
        );
    }
}

