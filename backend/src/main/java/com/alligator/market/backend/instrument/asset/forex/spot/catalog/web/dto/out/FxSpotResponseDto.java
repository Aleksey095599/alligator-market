package com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.out;

import com.alligator.market.domain.instrument.asset.forex.spot.model.FxSpotTenor;

/**
 * DTO ответа для инструментов FOREX_SPOT (out).
 */
public record FxSpotResponseDto(
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
