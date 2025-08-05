package com.alligator.market.domain.quote;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Глобальная модель тика котировки для валютной пары.
 * Аналогична Avro-схеме {@link com.alligator.market.domain.avro.QuoteTickAvro QuoteTickAvro}.
 */
public record QuoteTick(

        // Внутренний код инструмента
        String instrumentInternalCode,

        BigDecimal bid,
        BigDecimal ask,

        // Время тика котировки
        OffsetDateTime ts,

        // Внутренний код провайдера
        String providerCode
) {}
