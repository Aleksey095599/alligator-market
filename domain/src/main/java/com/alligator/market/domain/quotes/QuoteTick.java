package com.alligator.market.domain.quotes;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Модель тика котировки для валютной пары.
 * Аналогична Avro-схеме {@link com.alligator.market.domain.avro.quotes.stream.QuoteTick Avro QuoteTick}.
 */
public record QuoteTick(

        String pair,
        BigDecimal bid,
        BigDecimal ask,
        Instant ts,
        String provider

) {}
