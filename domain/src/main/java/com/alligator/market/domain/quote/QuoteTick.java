package com.alligator.market.domain.quote;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Глобальная модель тика котировки для валютной пары.
 * Аналогична Avro-схеме {@link com.alligator.market.domain.avro.QuoteTickAvro QuoteTickAvro}.
 */
public record QuoteTick(

        String internalInstrumentCode,
        BigDecimal bid,
        BigDecimal ask,
        Instant ts,
        String provider
) {}
