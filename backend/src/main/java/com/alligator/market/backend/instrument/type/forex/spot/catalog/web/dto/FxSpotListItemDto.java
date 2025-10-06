package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

/**
 * DTO для передачи списка инструментов FX_SPOT с кодом и символом.
 */
public record FxSpotListItemDto(
        // Применяется только для передачи данных вовне, проверки не нужны
        String code,
        String symbol,
        String baseCurrency,
        String quoteCurrency,
        Integer quoteDecimal,
        ValueDateCode valueDateCode
) {}
