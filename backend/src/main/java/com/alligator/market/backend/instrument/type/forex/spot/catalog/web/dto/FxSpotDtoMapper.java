package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.springframework.stereotype.Component;

/**
 * Маппер: модель ⇄ DTO.
 */
@Component
public class FxSpotDtoMapper {

    /** Преобразует доменную модель в DTO элемента списка. */
    public FxSpotListItemDto toListItemDto(FxSpot model) {
        return new FxSpotListItemDto(
                model.code(),
                model.symbol(),
                model.base().code().value(),
                model.quote().code().value(),
                model.quoteDecimal(),
                model.valueDateCode()
        );
    }
}

