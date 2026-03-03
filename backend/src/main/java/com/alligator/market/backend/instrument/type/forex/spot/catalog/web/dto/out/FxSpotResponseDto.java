package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.out;

import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpotTenor;

/**
 * DTO ответа для инструментов FX_SPOT (out).
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
