package com.alligator.market.backend.instrument_catalog.fx.spot.web.dto;

import com.alligator.market.domain.instrument.type.fx.spot.model.ValueDateCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO инструмента FX SPOT.
 */
public record FxSpotDto(

        @NotBlank
        String internalCode,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}$")
        String pairCode,

        @NotNull
        ValueDateCode valueDateCode
) {}
