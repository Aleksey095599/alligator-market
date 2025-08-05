package com.alligator.market.domain.quote;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Глобальная модель тика котировки для валютной пары.
 * Аналогична Avro-схеме {@link com.alligator.market.domain.avro.QuoteTickAvro QuoteTickAvro}.
 */
public record QuoteTick(

        /** Внутренний код инструмента, соответствует {@link Instrument#internalCode()} */
        String instrumentInternalCode,

        BigDecimal bid,
        BigDecimal ask,

        // Время тика котировки
        OffsetDateTime ts,

        /** Внутренний код провайдера, соответствует {@link ProviderProfile#providerCode()} */
        String provider
) {}
