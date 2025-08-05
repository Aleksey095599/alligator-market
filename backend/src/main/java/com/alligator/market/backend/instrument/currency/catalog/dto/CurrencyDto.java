package com.alligator.market.backend.instrument.currency.catalog.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для добавления/представления валюты.
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

