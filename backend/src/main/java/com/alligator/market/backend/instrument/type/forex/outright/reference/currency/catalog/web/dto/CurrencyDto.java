package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO валюты.
 */
public record CurrencyDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String code,

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

