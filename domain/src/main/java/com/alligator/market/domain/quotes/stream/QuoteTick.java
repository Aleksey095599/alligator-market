package com.alligator.market.domain.quotes.stream;

import java.math.BigDecimal;
import java.time.Instant;

/** Один тик котировки (bid/ask) по валютной паре. */
public record QuoteTick(

        String pair,
        BigDecimal bid,
        BigDecimal ask,
        Instant ts,
        String provider

) {}
