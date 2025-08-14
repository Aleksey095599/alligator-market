package com.alligator.market.domain.quote;

import com.alligator.market.domain.avro.QuoteTickAvro;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Глобальная модель тика котировки для валютной пары.
 * Соответствует Avro-схеме {@link QuoteTickAvro}.
 *
 * @param instrumentCode соответствует {@link Instrument#code()}
 * @param providerCode           соответствует {@link ProviderProfile#providerCode()}
 */
public record QuoteTick(

        String instrumentCode,
        BigDecimal bid,
        BigDecimal ask,
        Instant timestamp,
        String providerCode
) {}
