package com.alligator.core.fx.model;

import java.math.BigDecimal;
import java.time.Instant;

/* Модель котировки валютной пары. */
public record CurrencyQuote (
        Long pairId,
        BigDecimal bid,
        BigDecimal ask,
        short providerPriority,
        Instant ts
) {}

