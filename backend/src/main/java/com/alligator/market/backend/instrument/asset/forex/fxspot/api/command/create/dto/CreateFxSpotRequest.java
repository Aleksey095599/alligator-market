package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.dto;

import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * API-запрос на создание инструмента FOREX_SPOT.
 */
public record CreateFxSpotRequest(
        @NotBlank String baseCurrency,
        @NotBlank String quoteCurrency,
        @NotNull FxSpotTenor tenor,
        @NotNull @Min(0) @Max(10) Integer defaultQuoteFractionDigits
) {
}
