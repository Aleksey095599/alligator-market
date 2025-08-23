package com.alligator.market.backend.instrument.type.forex.outright.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
import jakarta.validation.constraints.*;

/**
 * Основной DTO инструмента FX_OUTRIGHT.
 */
public record FxOutrightDto(

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
) {}
