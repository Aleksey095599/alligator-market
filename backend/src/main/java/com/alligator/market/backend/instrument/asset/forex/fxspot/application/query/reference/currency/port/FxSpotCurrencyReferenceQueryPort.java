package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

public interface FxSpotCurrencyReferenceQueryPort {
    boolean referencesCurrency(CurrencyCode currencyCode);
}
