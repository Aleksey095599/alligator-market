package com.alligator.market.backend.instrument_catalog.currency_pair.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO обновления валютной пары.
 */
public record PairUpdateDto(

        @NotNull
        @Min(0) @Max(10)
        Integer decimal
) {}
