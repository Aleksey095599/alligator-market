package com.alligator.market.backend.instrument.type.fx.outright.reference.currency_pair.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO создания валютной пары.
 */
public record PairCreateDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String base,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String quote,

        @NotNull
        @Min(0) @Max(10)
        Integer decimal
) {}
