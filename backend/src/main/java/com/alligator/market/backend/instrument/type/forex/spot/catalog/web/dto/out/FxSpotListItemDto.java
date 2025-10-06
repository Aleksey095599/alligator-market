package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.out;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

/**
 * DTO для передачи списка инструментов FX_SPOT.
 */
public record FxSpotListItemDto(
        String symbol,
        String baseCurrency,
        String quoteCurrency,
        Integer quoteDecimal,
        ValueDateCode valueDateCode
) {}
