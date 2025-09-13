package com.alligator.market.domain.quote;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Глобальная модель тика котировки для валютной пары.
 *
 * @param instrumentCode соответствует {@link Instrument#code()}
 * @param providerCode   соответствует {@link ProviderStaticInfo#providerCode()}
 */
public record QuoteTick(

        String instrumentCode,
        BigDecimal bid,
        BigDecimal ask,
        Instant timestamp,
        String providerCode
) {}
