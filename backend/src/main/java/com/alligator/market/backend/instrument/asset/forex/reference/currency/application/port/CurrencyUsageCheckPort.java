package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

/**
 * Порт проверки использования валюты во внешних фичах/агрегатах.
 */
public interface CurrencyUsageCheckPort {

    /**
     * Проверка использования валюты во внешних фичах/агрегатах.
     */
    boolean isUsed(CurrencyCode currencyCode);
}
