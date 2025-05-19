package com.alligator.market.backend.currency.dto;

import jakarta.validation.constraints.*;

/* DTO для добавления валюты. */
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
) {
}

