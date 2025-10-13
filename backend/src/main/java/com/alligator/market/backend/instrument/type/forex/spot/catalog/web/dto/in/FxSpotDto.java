package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import jakarta.validation.constraints.*;

/**
 * Основной DTO инструмента FX_SPOT.
 */
public record FxSpotDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String baseCurrency,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String quoteCurrency,

        @NotNull
        FxSpotValueDate valueDate,

        @NotNull
        @Min(0) @Max(10)
        Integer defaultQuoteFractionDigits
) {}
