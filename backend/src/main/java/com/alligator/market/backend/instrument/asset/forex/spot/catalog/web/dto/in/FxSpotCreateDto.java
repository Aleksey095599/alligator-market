package com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.in;

import com.alligator.market.domain.instrument.market.forex.contract.spot.model.FxSpotTenor;

/**
 * DTO создания инструмента FX_SPOT (in).
 */
public record FxSpotCreateDto(
        String baseCurrency,
        String quoteCurrency,
        FxSpotTenor tenor,
        Integer defaultQuoteFractionDigits
) {
}
