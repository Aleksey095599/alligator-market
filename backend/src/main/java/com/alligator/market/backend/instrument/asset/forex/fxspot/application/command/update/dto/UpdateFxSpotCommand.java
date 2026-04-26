package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.dto;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Команда use case обновления инструмента FOREX_SPOT.
 */
public record UpdateFxSpotCommand(
        InstrumentCode instrumentCode,
        Integer defaultQuoteFractionDigits
) {
    public UpdateFxSpotCommand {
        instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        defaultQuoteFractionDigits = Objects.requireNonNull(
                defaultQuoteFractionDigits,
                "defaultQuoteFractionDigits must not be null"
        );
    }
}
