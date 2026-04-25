package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.dto;

import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;

/**
 * Команда use case создания инструмента FOREX_SPOT.
 */
public record CreateFxSpotCommand(
        String baseCurrency,
        String quoteCurrency,
        FxSpotTenor tenor,
        Integer defaultQuoteFractionDigits
) {
}
