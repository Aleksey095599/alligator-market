package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.dto;

import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * HTTP-запрос на создание инструмента FOREX_SPOT.
 */
public record CreateFxSpotRequest(
        @NotBlank(message = "baseCurrency must not be blank")
        String baseCurrency,

        @NotBlank(message = "quoteCurrency must not be blank")
        String quoteCurrency,

        @NotNull(message = "tenor must not be null")
        FxSpotTenor tenor,

        @NotNull(message = "defaultQuoteFractionDigits must not be null")
        @Min(value = 0, message = "defaultQuoteFractionDigits must be greater than or equal to 0")
        @Max(value = 10, message = "defaultQuoteFractionDigits must be less than or equal to 10")
        Integer defaultQuoteFractionDigits
) {
}
