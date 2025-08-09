package com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO валютной пары.
 */
public record PairDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String base,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String quote,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}$")
        String pairCode,

        @NotNull
        @Min(0) @Max(10)
        Integer decimal
) {}
