package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create;

import com.alligator.market.domain.instrument.catalog.forex.fxspot.classification.FxSpotTenor;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Команда use case создания инструмента FOREX_SPOT.
 */
public record CreateFxSpotCommand(
        CurrencyCode baseCurrencyCode,
        CurrencyCode quoteCurrencyCode,
        FxSpotTenor tenor,
        Integer defaultQuoteFractionDigits
) {
    public CreateFxSpotCommand {
        Objects.requireNonNull(baseCurrencyCode, "baseCurrencyCode must not be null");
        Objects.requireNonNull(quoteCurrencyCode, "quoteCurrencyCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");
        Objects.requireNonNull(defaultQuoteFractionDigits, "defaultQuoteFractionDigits must not be null");
    }
}
