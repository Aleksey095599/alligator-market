package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

public record UpdateFxSpotCommand(
        InstrumentCode instrumentCode,
        Integer defaultQuoteFractionDigits
) {
    public UpdateFxSpotCommand {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(defaultQuoteFractionDigits, "defaultQuoteFractionDigits must not be null");
    }
}
