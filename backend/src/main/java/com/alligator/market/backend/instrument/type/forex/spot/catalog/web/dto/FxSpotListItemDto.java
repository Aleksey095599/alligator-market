package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

/**
 * DTO элемента списка FX_SPOT с кодом и символом.
 */
public record FxSpotListItemDto(
        String code,
        String symbol,
        String baseCurrency,
        String quoteCurrency,
        Integer quoteDecimal,
        ValueDateCode valueDateCode
) {
}
