package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto;

import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;

/**
 * HTTP-ответ со сведениями об инструменте FOREX_SPOT.
 */
public record FxSpotListResponse(
        String instrumentCode,
        String symbol,
        String assetClass,
        String contractType,
        String baseCurrency,
        String quoteCurrency,
        Integer defaultQuoteFractionDigits,
        FxSpotTenor tenor
) {
}
