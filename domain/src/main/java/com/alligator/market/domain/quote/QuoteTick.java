package com.alligator.market.domain.quote;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Глобальная модель тика котировки для валютной пары.
 *
 * @param instrumentCode соответствует {@link Instrument#code()}
 * @param providerCode   соответствует {@link MarketDataProvider#providerCode()}
 */
public record QuoteTick(

        String instrumentCode,
        BigDecimal bid,
        BigDecimal ask,
        Instant timestamp,
        String providerCode
) {}
