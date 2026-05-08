package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateFxSpotRequest(
        @NotNull @Min(0) @Max(10) Integer defaultQuoteFractionDigits
) {
}
