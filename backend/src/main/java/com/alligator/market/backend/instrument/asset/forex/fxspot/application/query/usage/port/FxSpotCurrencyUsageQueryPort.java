package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

/**
 * Query-port проверки использования валюты в инструментах FOREX_SPOT.
 */
public interface FxSpotCurrencyUsageQueryPort {

    /**
     * Проверить использование валюты хотя бы в одном FX_SPOT инструменте.
     */
    boolean existsByCurrencyCode(CurrencyCode currencyCode);
}
