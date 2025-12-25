package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.out.FxSpotListItemDto;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.Objects;

/**
 * Маппер: модель ↔ DTO.
 */
public class FxSpotDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private FxSpotDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Преобразует доменную модель в DTO для списка.
     */
    public static FxSpotListItemDto toListItemDto(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        return new FxSpotListItemDto(
                fxSpot.instrumentCode(),
                fxSpot.instrumentSymbol(),
                fxSpot.base().code().value(),
                fxSpot.quote().code().value(),
                fxSpot.defaultQuoteFractionDigits(),
                fxSpot.valueDate()
        );
    }
}

