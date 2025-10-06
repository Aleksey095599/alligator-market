package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

/**
 * DTO для передачи списка инструментов FX_SPOT.
 */
public record FxSpotListItemDto(
        // Применяется только для получения данных из доменной модели и передачи во frontend => проверки не обязательны
        String symbol,
        String baseCurrency,
        String quoteCurrency,
        Integer quoteDecimal,
        ValueDateCode valueDateCode
) {}
