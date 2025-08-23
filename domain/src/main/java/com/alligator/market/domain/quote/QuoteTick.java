package com.alligator.market.domain.quote;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.model.profile.ProviderProfile;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Глобальная модель тика котировки для валютной пары.
 *
 * @param instrumentCode соответствует {@link Instrument#getCode()}
 * @param providerCode   соответствует {@link ProviderProfile#providerCode()}
 */
public record QuoteTick(

        String instrumentCode,
        BigDecimal bid,
        BigDecimal ask,
        Instant timestamp,
        String providerCode
) {}
