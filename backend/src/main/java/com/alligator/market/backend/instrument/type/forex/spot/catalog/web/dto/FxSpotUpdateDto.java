package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для обновления инструмента FX_SPOT.
 */
public record FxSpotUpdateDto(

        @NotNull
        @Min(0) @Max(10)
        Integer quoteDecimal
) {}

