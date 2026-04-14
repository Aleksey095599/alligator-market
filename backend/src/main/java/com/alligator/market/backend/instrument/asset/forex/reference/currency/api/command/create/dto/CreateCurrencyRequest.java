package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO use case создания валюты.
 */
public record CreateCurrencyRequest(
        @NotBlank(message = "code must not be blank")
        String code,

        @NotBlank(message = "name must not be blank")
        String name,

        @NotBlank(message = "country must not be blank")
        String country,

        @NotNull(message = "fractionDigits must not be null")
        @Min(value = 0, message = "fractionDigits must be greater than or equal to 0")
        @Max(value = 10, message = "fractionDigits must be less than or equal to 10")
        Integer fractionDigits
) {
}
