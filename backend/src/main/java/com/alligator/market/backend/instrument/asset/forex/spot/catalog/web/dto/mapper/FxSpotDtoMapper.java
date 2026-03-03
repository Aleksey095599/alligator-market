package com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.out.FxSpotResponseDto;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpot;

import java.util.Objects;

/**
 * Маппер DTO для инструмента FX_SPOT.
 */
public class FxSpotDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private FxSpotDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Модель --> DTO ответа.
     */
    public static FxSpotResponseDto toFxSpotResponseDto(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        return new FxSpotResponseDto(
                fxSpot.instrumentCode().value(),
                fxSpot.instrumentSymbol().value(),
                fxSpot.base().code().value(),
                fxSpot.quote().code().value(),
                fxSpot.defaultQuoteFractionDigits(),
                fxSpot.tenor()
        );
    }
}
