package com.alligator.market.backend.currency_pairs.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для представления валютной пары.
 */
public record PairDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String code1,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String code2,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}$")
        String pair,

        @NotNull
        @Min(0) @Max(10)
        Integer decimal

) {
}
