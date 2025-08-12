package com.alligator.market.backend.instrument_catalog.fx.outright.web.dto;

import com.alligator.market.domain.instrument.type.fx.outright.model.ValueDateCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO инструмента FX OUTRIGHT.
 */
public record FxOutrightDto(

        @NotBlank
        String internalCode,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}$")
        String pairCode,

        @NotNull
        ValueDateCode valueDateCode
) {}
