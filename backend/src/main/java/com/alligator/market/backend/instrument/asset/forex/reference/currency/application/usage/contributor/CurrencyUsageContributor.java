package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

public interface CurrencyUsageContributor {
    boolean isUsed(CurrencyCode currencyCode);
}
