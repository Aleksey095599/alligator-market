package com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.web.dto.in;

import com.alligator.market.domain.instrument.asset.forex.fxspot.tenor.FxSpotTenor;

/**
 * DTO создания инструмента FOREX_SPOT (in).
 */
public record FxSpotCreateDto(
        String baseCurrency,
        String quoteCurrency,
        FxSpotTenor tenor,
        Integer defaultQuoteFractionDigits
) {
}
