package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.dto;

import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;

import java.util.Objects;

/**
 * Команда use case создания инструмента FOREX_SPOT.
 */
public record CreateFxSpotCommand(
        String baseCurrency,
        String quoteCurrency,
        FxSpotTenor tenor,
        Integer defaultQuoteFractionDigits
) {
    public CreateFxSpotCommand {
        baseCurrency = Objects.requireNonNull(baseCurrency, "baseCurrency must not be null");
        quoteCurrency = Objects.requireNonNull(quoteCurrency, "quoteCurrency must not be null");
        tenor = Objects.requireNonNull(tenor, "tenor must not be null");
        defaultQuoteFractionDigits = Objects.requireNonNull(
                defaultQuoteFractionDigits,
                "defaultQuoteFractionDigits must not be null"
        );
    }
}
