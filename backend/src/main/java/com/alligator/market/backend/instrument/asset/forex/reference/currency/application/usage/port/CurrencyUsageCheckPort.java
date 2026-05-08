package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

public interface CurrencyUsageCheckPort {
    boolean isUsed(CurrencyCode currencyCode);
}
