package com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.web.dto.out;

import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpotTenor;

/**
 * DTO ответа для инструментов FOREX_SPOT (out).
 */
public record FxSpotResponseDto(
        String instrumentCode,
        String symbol,
        String baseCurrency,
        String quoteCurrency,
        Integer defaultQuoteFractionDigits,
        FxSpotTenor tenor
) {
}
