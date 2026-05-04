package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto;

import com.alligator.market.domain.instrument.catalog.forex.fxspot.classification.FxSpotTenor;

/**
 * API-ответ для списка инструментов FOREX_SPOT.
 */
public record FxSpotListItemResponse(
        String instrumentCode,
        String symbol,
        String asset,
        String product,
        String baseCurrency,
        String quoteCurrency,
        Integer defaultQuoteFractionDigits,
        FxSpotTenor tenor
) {
}
