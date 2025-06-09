package com.alligator.market.core.fx.dto;

import java.math.BigDecimal;
import java.time.Instant;

/** DTO котировки для валютной пары (например, для отправки с backend на frontend). */
public record CurrencyQuoteDto(
        Long pairId,
        BigDecimal bid,
        BigDecimal ask,
        Instant ts
) {}
