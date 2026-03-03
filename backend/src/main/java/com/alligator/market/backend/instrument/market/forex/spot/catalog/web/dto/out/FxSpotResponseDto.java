package com.alligator.market.backend.instrument.market.forex.spot.catalog.web.dto.out;

import com.alligator.market.domain.instrument.market.forex.spot.model.FxSpotTenor;

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
