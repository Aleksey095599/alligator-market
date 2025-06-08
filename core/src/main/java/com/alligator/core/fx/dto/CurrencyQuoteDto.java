package com.alligator.core.fx.dto;

import java.math.BigDecimal;
import java.time.Instant;

/* DTO котировки для отправки на клиент. */
public record CurrencyQuoteDto(
        Long pairId,
        BigDecimal bid,
        BigDecimal ask,
        Instant ts
) {}
