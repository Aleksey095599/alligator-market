package com.alligator.core.model;

import java.math.BigDecimal;
import java.time.Instant;

/* Нормализованный тик (котировка) валютной пары. */
public record CurrencyQuote (
        Long pairId,
        BigDecimal bid,
        BigDecimal ask,
        short providerPriority,
        Instant ts
) {}

