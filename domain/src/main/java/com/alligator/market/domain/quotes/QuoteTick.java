package com.alligator.market.domain.quotes;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Модель тика котировки для валютной пары.
 * Аналогична Avro-схеме {@link com.alligator.market.domain.avro.QuoteTickAvro QuoteTickAvro}.
 */
public record QuoteTick(

        /* Символ (валютная пара или другой инструмент). */
        String symbol,
        BigDecimal bid,
        BigDecimal ask,
        Instant ts,
        String provider
) {}
