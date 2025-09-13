package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO обновления.
 */
public record UpdateCurrencyDto(

        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(max = 100)
        String country,

        @NotNull
        @Min(0) @Max(10)
        Integer decimal
) {}
