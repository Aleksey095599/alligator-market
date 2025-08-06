package com.alligator.market.domain.quote;

import com.alligator.market.domain.avro.QuoteTickAvro;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Глобальная модель тика котировки для валютной пары.
 * Соответствует Avro-схеме {@link QuoteTickAvro}.
 *
 * @param instrumentInternalCode соответствует {@link Instrument#internalCode()}
 * @param providerCode           соответствует {@link ProviderProfile#providerCode()}
 */
public record QuoteTick(

        String instrumentInternalCode,
        BigDecimal bid,
        BigDecimal ask,
        OffsetDateTime timestamp,
        String providerCode
) {}
