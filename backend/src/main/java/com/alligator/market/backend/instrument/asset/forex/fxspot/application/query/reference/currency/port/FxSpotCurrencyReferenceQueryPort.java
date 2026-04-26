package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

/**
 * Query-port проверки ссылок FX_SPOT на валюту.
 */
public interface FxSpotCurrencyReferenceQueryPort {

    /**
     * Проверить, ссылается ли хотя бы один FX_SPOT на валюту как base или quote.
     */
    boolean referencesCurrency(CurrencyCode currencyCode);
}
