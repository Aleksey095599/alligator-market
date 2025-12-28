package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotTenor;
import jakarta.validation.constraints.*;

/**
 * DTO создания инструмента FX_SPOT (in).
 */
public record FxSpotCreateDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String baseCurrency,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String quoteCurrency,

        @NotNull
        FxSpotTenor tenor,

        @NotNull
        @Min(0) @Max(10)
        Integer defaultQuoteFractionDigits
) {
}
