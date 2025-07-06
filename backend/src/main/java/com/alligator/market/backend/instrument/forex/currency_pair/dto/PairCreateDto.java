package com.alligator.market.backend.instrument.forex.currency_pair.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для создания валютной пары.
 */
public record PairCreateDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String code1,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String code2,

        @NotNull
        @Min(0) @Max(10)
        Integer decimal

) {
}
