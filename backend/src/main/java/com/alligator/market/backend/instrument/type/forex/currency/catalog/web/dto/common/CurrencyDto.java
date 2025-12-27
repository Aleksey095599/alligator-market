package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Общий DTO валюты для in/out контрактов.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    private String code;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotNull
    @Min(0) @Max(10)
    private Integer fractionDigits;
}
