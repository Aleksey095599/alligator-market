package com.alligator.market.backend.instrument.currency_pair.catalog.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для представления валютной пары.
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
