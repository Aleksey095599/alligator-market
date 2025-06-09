package com.alligator.market.core.fx.model;

import java.math.BigDecimal;
import java.time.Instant;

/** Доменная модель котировки валютной пары с набором соответствующих полей. */
public record CurrencyQuote (
        Long pairId,
        BigDecimal bid,
        BigDecimal ask,
        short providerPriority,
        Instant ts
) {}

