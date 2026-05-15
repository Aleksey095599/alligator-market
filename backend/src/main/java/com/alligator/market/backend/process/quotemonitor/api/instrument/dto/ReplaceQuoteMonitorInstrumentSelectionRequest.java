package com.alligator.market.backend.process.quotemonitor.api.instrument.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReplaceQuoteMonitorInstrumentSelectionRequest(

        @NotNull(message = "instrumentCodes must not be null")
        List<@NotBlank(message = "instrumentCode must not be blank") String> instrumentCodes
) {
}
