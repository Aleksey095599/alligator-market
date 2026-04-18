package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * API-запрос на обновление инструмента FOREX_SPOT.
 */
public record UpdateFxSpotRequest(
        @NotNull @Min(0) @Max(10) Integer defaultQuoteFractionDigits
) {
}
