package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in;

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
        Integer fractionDigits
) {
}
