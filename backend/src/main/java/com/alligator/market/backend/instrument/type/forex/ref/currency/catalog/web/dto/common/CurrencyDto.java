package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.common;

import jakarta.validation.constraints.*;

/**
 * Основной DTO валюты.
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
        Integer fractionDigits
) {}

