package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO элемента списка FX_SPOT с кодом и символом.
 */
public record FxSpotListItemDto(
        @NotBlank
        @Pattern(regexp = "^FX_SPOT_[A-Z]{3}[A-Z]{3}_(TOD|TOM|SPOT)$")
        String code,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}[A-Z]{3}_(TOD|TOM|SPOT)$")
        String symbol,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String baseCurrency,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String quoteCurrency,

        @NotNull
        @Min(0) @Max(10)
        Integer quoteDecimal,

        @NotNull
        ValueDateCode valueDateCode
) {
}
