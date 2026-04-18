package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * HTTP-запрос на обновление инструмента FOREX_SPOT.
 */
public record UpdateFxSpotRequest(
        @NotNull(message = "defaultQuoteFractionDigits must not be null")
        @Min(value = 0, message = "defaultQuoteFractionDigits must be greater than or equal to 0")
        @Max(value = 10, message = "defaultQuoteFractionDigits must be less than or equal to 10")
        Integer defaultQuoteFractionDigits
) {
}
