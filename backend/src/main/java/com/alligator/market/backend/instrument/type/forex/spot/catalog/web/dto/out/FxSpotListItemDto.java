package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.out;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;

/**
 * DTO для передачи списка инструментов FX_SPOT.
 */
public record FxSpotListItemDto(

        String symbol,
        String baseCurrency,
        String quoteCurrency,
        Integer defaultQuoteFractionDigits,
        FxSpotValueDate valueDate
) {}
