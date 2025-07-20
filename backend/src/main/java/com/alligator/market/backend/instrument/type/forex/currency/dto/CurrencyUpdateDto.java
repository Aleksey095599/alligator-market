package com.alligator.market.backend.instrument.type.forex.currency.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для обновления валюты.
 */
public record CurrencyUpdateDto(

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
