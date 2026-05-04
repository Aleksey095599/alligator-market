package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port;

import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;

/**
 * Query-port проверки ссылок FOREX_SPOT на валюту: то есть используется ли валюта в FOREX_SPOT как base или quote.
 */
public interface FxSpotCurrencyReferenceQueryPort {

    /**
     * Проверить, ссылается ли хотя бы один FOREX_SPOT на валюту как base или quote.
     */
    boolean referencesCurrency(CurrencyCode currencyCode);
}
